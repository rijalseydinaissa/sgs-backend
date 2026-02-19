package com.example.sgs_backend.application.expense.dto;
import com.example.sgs_backend.domain.expense.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id, String reference, String description,
        BigDecimal amount, String currency,
        LocalDate expenseDate,
        UUID categoryId, String categoryName,
        UUID supplierId, ExpenseStatus status,
        UUID approvedByUserId,
        LocalDate createdAt, String createdBy,
        LocalDate localDate, String by) {}
