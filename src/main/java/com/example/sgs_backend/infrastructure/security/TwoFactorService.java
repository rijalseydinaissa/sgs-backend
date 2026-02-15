package com.example.sgs_backend.infrastructure.security;

import com.example.sgs_backend.domain.common.exception.BusinessRuleException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Slf4j
public class TwoFactorService {

    private final GoogleAuthenticator googleAuth = new GoogleAuthenticator();

    /**
     * Génère un secret TOTP (clé complète)
     */
    public GoogleAuthenticatorKey generateKey() {
        return googleAuth.createCredentials();
    }

    /**
     * Génère l'URL du QR Code
     */
    public String generateQrCodeUrl(String username, GoogleAuthenticatorKey key) {
        try {
            return GoogleAuthenticatorQRGenerator
                    .getOtpAuthURL("SGS", username, key);
        } catch (Exception e) {
            log.error("Erreur génération QR Code 2FA", e);
            throw new BusinessRuleException("Impossible de générer le QR code 2FA");
        }
    }

    /**
     * Vérifie un code 2FA
     */
    public boolean verifyCode(String secret, String code) {
        if (code == null || !code.matches("\\d{6}")) return false;
        return googleAuth.authorize(secret, Integer.parseInt(code));
    }

    /**
     * Codes de secours
     */
    public String[] generateBackupCodes() {
        SecureRandom random = new SecureRandom();
        String[] codes = new String[8];
        for (int i = 0; i < 8; i++) {
            codes[i] = String.format("%04d-%04d",
                    random.nextInt(10000),
                    random.nextInt(10000));
        }
        return codes;
    }
}
