package com.example.sgs_backend.application.user.dto;

import com.example.sgs_backend.domain.user.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Requête création/modification d'un utilisateur")
public record UserRequest(
    @NotBlank @Size(min=3, max=60)
    @Pattern(regexp="^[a-zA-Z0-9._-]+$", message="Caractères autorisés : lettres, chiffres, . _ -")
    String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min=8) String password,
    @NotBlank @Size(max=80) String firstName,
    @NotBlank @Size(max=80) String lastName,
    @Pattern(regexp="^[+]?[0-9\\s\\-().]{7,20}$") String phone,
    @NotEmpty Set<RoleName> roles,
    UUID siteId
) {}
