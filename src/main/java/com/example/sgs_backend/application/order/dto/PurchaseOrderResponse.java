package com.example.sgs_backend.application.order.dto;
import com.example.sgs_backend.domain.order.PurchaseOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PurchaseOrderResponse(
    UUID id, String reference,
    UUID supplierId, String supplierName,
    LocalDate orderDate,
    PurchaseOrderStatus status,
    BigDecimal totalAmount, String currency
) {}
