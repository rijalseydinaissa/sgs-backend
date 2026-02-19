package com.example.sgs_backend.application.expense.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(
    @NotBlank String reference,
    @NotBlank String description,
    @NotNull BigDecimal amount,
    String currency,
    @NotNull LocalDate expenseDate,
    @NotNull UUID categoryId,
    UUID supplierId,
    String notes
) {}
