package com.example.sgs_backend.application.order.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record PurchaseOrderRequest(
    @NotBlank String reference,
    @NotNull UUID supplierId,
    @NotNull LocalDate orderDate
) {}
