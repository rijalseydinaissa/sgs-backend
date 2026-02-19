package com.example.sgs_backend.application.product.dto;

import jakarta.validation.constraints.*;

public record SupplierRequest(
    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9\\-]+$") String code,
    @NotBlank @Size(max = 150) String name,
    String phone,
    @Email String email,
    String contactPerson,
    String taxNumber,
    String addressStreet,
    String addressCity,
    String addressRegion,
    String addressCountry,
    Integer paymentTermsDays
) {}
