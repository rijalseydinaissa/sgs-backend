package com.example.sgs_backend.application.auth.dto;
import jakarta.validation.constraints.*;
public record ChangePasswordRequest(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 8) String newPassword,
    @NotBlank String confirmPassword
) {}
