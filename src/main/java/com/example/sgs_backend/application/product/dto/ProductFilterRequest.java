package com.example.sgs_backend.application.product.dto;

import com.example.sgs_backend.domain.product.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Filtres de recherche produits")
public record ProductFilterRequest(
    String        search,           // Recherche texte (nom, référence, barcode)
    UUID          categoryId,
    UUID          supplierId,
    UUID          siteId,
    ProductStatus status,
    Boolean       lowStockOnly,
    BigDecimal    minPrice,
    BigDecimal    maxPrice
) {}
