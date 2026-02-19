package com.example.sgs_backend.application.stock.dto;


import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementResponse(
    UUID id, UUID productId, String productName,
    MovementType movementType,
    BigDecimal quantityValue, Quantity.Unit quantityUnit,
    LocalDateTime movementDate,
    String reference, String notes,
    UUID fromSiteId, UUID toSiteId,
    BigDecimal stockBefore, BigDecimal stockAfter,
    LocalDateTime createdAt, String createdBy
) {}
