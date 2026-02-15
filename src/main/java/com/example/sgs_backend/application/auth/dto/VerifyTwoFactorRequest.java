package com.example.sgs_backend.application.auth.dto;
import jakarta.validation.constraints.*;
public record VerifyTwoFactorRequest(
    @NotBlank @Pattern(regexp = "\\d{6}") String code,
    @NotBlank String partialToken
) {}
