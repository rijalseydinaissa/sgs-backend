package com.example.sgs_backend.application.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requête de connexion")
public record LoginRequest(
    @NotBlank(message = "Nom d'utilisateur obligatoire") String username,
    @NotBlank(message = "Mot de passe obligatoire")      String password,
    @Schema(description = "Code 2FA — requis si 2FA activée")
    String twoFactorCode
) {}
