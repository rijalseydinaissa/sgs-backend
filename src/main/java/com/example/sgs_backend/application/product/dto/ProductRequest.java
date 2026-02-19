package com.example.sgs_backend.application.product.dto;


import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.StockEvaluationMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Requête de création / modification d'un produit")
public record ProductRequest(

    @NotBlank @Size(max = 60)
    @Pattern(regexp = "^[A-Z0-9\\-]+$")
    String reference,

    @NotBlank @Size(max = 200)
    String name,

    String description,
    String barcode,

    @NotNull UUID categoryId,

    // Prix avec devise explicite
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal purchasePrice,

    @NotNull @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal sellingPrice,

    @Size(max = 3) @Schema(example = "XOF")
    String currency,

    // Stock avec unité Quantity.Unit
    @NotNull @Min(0)
    BigDecimal minimumStockValue,

    @NotNull
    @Schema(example = "PIECE", description = "PIECE, KG, GRAM, LITER, ML, METER, CM, CARTON, PACK, BOX")
    Quantity.Unit minimumStockUnit,

    BigDecimal maximumStockValue,
    Quantity.Unit maximumStockUnit,

    BigDecimal reorderPointValue,
    Quantity.Unit reorderPointUnit,

    StockEvaluationMethod evaluationMethod,

    boolean hasExpiryDate,
    Integer expiryAlertDays,

    UUID mainSupplierId,
    String imageUrl,
    UUID siteId
) {}
