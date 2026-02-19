package com.example.sgs_backend.application.product.dto;


import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSummaryResponse(
    UUID          id,
    String        reference,
    String        name,
    String        barcode,
    String        categoryName,
    BigDecimal    sellingPrice,
    String        currency,
    BigDecimal    currentStock,
    Quantity.Unit stockUnit,
    boolean       lowStock,
    ProductStatus status
) {}
