package com.example.sgs_backend.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    String  accessToken, String refreshToken, String tokenType,
    Instant expiresAt,   UUID   userId,       String username,
    String  fullName,    String email,
    Set<String> roles,   boolean twoFactorEnabled, boolean requiresTwoFactor
) {
    public static AuthResponse requiresTwoFactor(String partialToken) {
        return new AuthResponse(partialToken, null, "Bearer",
            null, null, null, null, null, null, true, true);
    }
}
