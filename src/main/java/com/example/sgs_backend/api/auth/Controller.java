package com.example.sgs_backend.api.auth;


import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.application.auth.AuthService;
import com.example.sgs_backend.application.auth.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentification JWT + 2FA")
public class Controller {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion — retourne JWT ou demande code 2FA")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req, HttpServletRequest http) {
        String ip = getIp(http);
        return ResponseEntity.ok(ApiResponse.success(authService.login(req, ip), "Connexion réussie"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renouveler le token d'accès avec le refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        return ResponseEntity.ok(ApiResponse.success(authService.refreshToken(req)));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Déconnexion — révoque le token JWT")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest http) {
        authService.logout(http.getHeader("Authorization"));
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PostMapping("/2fa/verify")
    @Operation(summary = "Vérifier le code 2FA (2ème étape du login)")
    public ResponseEntity<ApiResponse<AuthResponse>> verify2fa(@Valid @RequestBody VerifyTwoFactorRequest req) {
        return ResponseEntity.ok(ApiResponse.success(authService.verifyTwoFactor(req)));
    }

    @PostMapping("/2fa/setup")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Initialiser la 2FA — obtenir le QR code")
    public ResponseEntity<ApiResponse<TwoFactorSetupResponse>> setup2fa(@RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(authService.setupTwoFactor(userId)));
    }

    @PostMapping("/2fa/confirm")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Confirmer et activer la 2FA avec le premier code")
    public ResponseEntity<ApiResponse<Void>> confirm2fa(@RequestParam UUID userId, @RequestParam String code) {
        authService.confirmTwoFactor(userId, code);
        return ResponseEntity.ok(ApiResponse.success(null, "2FA activée ✅"));
    }

    @DeleteMapping("/2fa/disable")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Désactiver la 2FA")
    public ResponseEntity<ApiResponse<Void>> disable2fa(@RequestParam UUID userId, @RequestParam String code) {
        authService.disableTwoFactor(userId, code);
        return ResponseEntity.ok(ApiResponse.success(null, "2FA désactivée"));
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Changer son mot de passe")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam UUID userId, @Valid @RequestBody ChangePasswordRequest req) {
        authService.changePassword(userId, req);
        return ResponseEntity.ok(ApiResponse.success(null, "Mot de passe modifié ✅"));
    }

    private String getIp(HttpServletRequest req) {
        String fwd = req.getHeader("X-Forwarded-For");
        return fwd != null ? fwd.split(",")[0].trim() : req.getRemoteAddr();
    }
}

