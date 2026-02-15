package com.example.sgs_backend.application.auth;

import com.example.sgs_backend.application.auth.dto.*;
import com.example.sgs_backend.application.auth.port.TokenBlacklistPort;
import com.example.sgs_backend.application.auth.port.UserRepository;
import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.user.User;
import com.example.sgs_backend.infrastructure.security.JwtService;
import com.example.sgs_backend.infrastructure.security.TwoFactorService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsService  userDetailsService;
    private final JwtService jwtService;
    private final TwoFactorService twoFactorService;
    private final TokenBlacklistPort tokenBlacklist;
    private final AuthenticationManager authManager;
    private final PasswordEncoder     passwordEncoder;

    @Transactional
    public AuthResponse login(LoginRequest req, String clientIp) {
        User user = userRepository.findByUsername(req.username())
            .or(() -> userRepository.findByEmail(req.username()))
            .orElseThrow(() -> new BadCredentialsException("Identifiants incorrects"));

        if (user.isAccountLocked())  throw new LockedException("Compte verrouillé — réessayez dans 30 min");
        if (!user.isActive())        throw new DisabledException("Compte désactivé");

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            user.recordFailedLogin();
            userRepository.save(user);
            throw new BadCredentialsException("Identifiants incorrects");
        }

        // 2FA activée mais code absent → token partiel
        if (user.isTwoFactorEnabled() && (req.twoFactorCode() == null || req.twoFactorCode().isBlank())) {
            var ud = userDetailsService.loadUserByUsername(user.getUsername());
            return AuthResponse.requiresTwoFactor(jwtService.generatePartialToken(ud));
        }

        // 2FA activée et code fourni → valider
        if (user.isTwoFactorEnabled() &&
            !twoFactorService.verifyCode(user.getTwoFactorSecret(), req.twoFactorCode()))
            throw new BusinessRuleException("Code 2FA invalide", "ERR_INVALID_2FA");

        user.recordSuccessfulLogin(clientIp);
        userRepository.save(user);
        return buildFullResponse(user);
    }

    @Transactional
    public AuthResponse verifyTwoFactor(VerifyTwoFactorRequest req) {
        String username;
        try { username = jwtService.extractUsername(req.partialToken()); }
        catch (Exception e) { throw new BusinessRuleException("Token partiel invalide", "ERR_TOKEN_INVALID"); }

        if (!jwtService.isPartialToken(req.partialToken()))
            throw new BusinessRuleException("Token fourni n'est pas partiel", "ERR_TOKEN_INVALID");

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));

        if (!twoFactorService.verifyCode(user.getTwoFactorSecret(), req.code()))
            throw new BusinessRuleException("Code 2FA invalide", "ERR_INVALID_2FA");

        long remaining = jwtService.extractExpiration(req.partialToken()).getTime() - System.currentTimeMillis();
        tokenBlacklist.blacklist(req.partialToken(), Math.max(remaining, 1000));
        return buildFullResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest req) {
        String username;
        try { username = jwtService.extractUsername(req.refreshToken()); }
        catch (Exception e) { throw new BusinessRuleException("Refresh token invalide", "ERR_TOKEN_INVALID"); }

        if (tokenBlacklist.isBlacklisted(req.refreshToken()))
            throw new BusinessRuleException("Refresh token révoqué", "ERR_TOKEN_INVALID");

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));
        return buildFullResponse(user);
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;
        String token  = authHeader.substring(7);
        long remaining = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();
        if (remaining > 0) tokenBlacklist.blacklist(token, remaining);
    }

    @Transactional
    public TwoFactorSetupResponse setupTwoFactor(UUID userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.isTwoFactorEnabled())
            throw new BusinessRuleException("2FA déjà activée");

        GoogleAuthenticatorKey key = twoFactorService.generateKey(); // renvoie GoogleAuthenticatorKey
        String secret = key.getKey(); // convertir en String

        user.setTwoFactorSecret(secret);
        userRepository.save(user);

        return new TwoFactorSetupResponse(
                secret,
                twoFactorService.generateQrCodeUrl(user.getUsername(), key),
                twoFactorService.generateBackupCodes()
        );
    }


    @Transactional
    public void confirmTwoFactor(UUID userId, String code) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (!twoFactorService.verifyCode(user.getTwoFactorSecret(), code))
            throw new BusinessRuleException("Code invalide", "ERR_INVALID_2FA");
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void disableTwoFactor(UUID userId, String code) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (!user.isTwoFactorEnabled()) throw new BusinessRuleException("2FA non activée");
        if (!twoFactorService.verifyCode(user.getTwoFactorSecret(), code))
            throw new BusinessRuleException("Code 2FA invalide", "ERR_INVALID_2FA");
        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest req) {
        if (!req.newPassword().equals(req.confirmPassword()))
            throw new BusinessRuleException("Les mots de passe ne correspondent pas");
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword()))
            throw new BusinessRuleException("Mot de passe actuel incorrect");
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        user.setPasswordChangedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }

    // ── Helper ────────────────────────────────────────────────────
    private AuthResponse buildFullResponse(User user) {
        var ud      = userDetailsService.loadUserByUsername(user.getUsername());
        String at   = jwtService.generateAccessToken(ud);
        String rt   = jwtService.generateRefreshToken(ud);
        Date expiry = jwtService.extractExpiration(at);
        Set<String> roles = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
        return new AuthResponse(at, rt, "Bearer", expiry.toInstant(),
            user.getId(), user.getUsername(), user.getFullName(),
            user.getEmail(), roles, user.isTwoFactorEnabled(), false);
    }
}
