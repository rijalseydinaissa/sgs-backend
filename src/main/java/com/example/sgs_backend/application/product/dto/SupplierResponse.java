package com.example.sgs_backend.application.product.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierResponse(
    UUID   id, String code, String name,
    String phone, String email, String contactPerson,
    String taxNumber,
    String addressCity, String addressCountry,
    Integer paymentTermsDays, Double averageRating,
    boolean active, LocalDateTime createdAt
) {}
