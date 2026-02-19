package com.example.sgs_backend.application.stock.dto;


import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementRequest(
    @NotNull UUID productId,
    @NotNull MovementType movementType,
    @NotNull BigDecimal quantityValue,
    @NotNull Quantity.Unit quantityUnit,
    LocalDateTime movementDate,
    String reference,
    String notes,
    UUID fromSiteId,
    UUID toSiteId
) {}
