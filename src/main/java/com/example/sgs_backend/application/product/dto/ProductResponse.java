package com.example.sgs_backend.application.product.dto;


import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.ProductStatus;
import com.example.sgs_backend.domain.product.StockEvaluationMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
    UUID    id,
    String  reference,
    String  name,
    String  description,
    String  barcode,
    String  imageUrl,

    UUID    categoryId,
    String  categoryName,
    String  categoryPath,

    // Prix Money VO
    BigDecimal purchasePrice,
    BigDecimal sellingPrice,
    String     currency,
    double     marginPercent,

    // Stock Quantity VO
    BigDecimal      currentStockValue,
    Quantity.Unit   currentStockUnit,
    BigDecimal      minimumStockValue,
    Quantity.Unit   minimumStockUnit,
    boolean         lowStock,
    boolean         outOfStock,
    BigDecimal      stockValue,
    StockEvaluationMethod evaluationMethod,

    UUID          mainSupplierId,
    String        mainSupplierName,
    UUID          siteId,
    ProductStatus status,
    boolean       hasExpiryDate,

    LocalDateTime createdAt,
    String        createdBy,
    LocalDateTime updatedAt
) {}
