package com.example.sgs_backend.application.customer.dto;
import com.example.sgs_backend.domain.customer.CustomerType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CustomerRequest(
    @NotBlank String code,
    @NotBlank String name,
    CustomerType customerType,
    String phone,
    @Email String email,
    String addressStreet,
    String addressCity,
    String addressCountry,
    BigDecimal creditLimit
) {}
