package com.example.sgs_backend.application.product.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record CategoryRequest(
    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "Code : majuscules, chiffres et tirets")
    String code,
    @NotBlank @Size(max = 150) String name,
    String description,
    String iconUrl,
    UUID parentId
) {}
