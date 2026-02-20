package com.example.sgs_backend.application.customer.dto;
import com.example.sgs_backend.domain.customer.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
    UUID id, String code, String name,
    CustomerType customerType,
    String phone, String email,
    String addressCity, String addressCountry,
    BigDecimal accountBalance, String currency,
    boolean active, LocalDateTime createdAt
) {}
