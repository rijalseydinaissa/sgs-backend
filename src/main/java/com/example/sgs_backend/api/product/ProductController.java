package com.example.sgs_backend.api.product;


import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.common.PageResponse;
import com.example.sgs_backend.application.product.ProductService;
import com.example.sgs_backend.application.product.dto.ProductRequest;
import com.example.sgs_backend.application.product.dto.ProductResponse;
import com.example.sgs_backend.application.product.dto.ProductSummaryResponse;
import com.example.sgs_backend.domain.product.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * ✅ extends BaseController<Product, UUID, ProductRequest, ProductResponse>
 *
 * Hérite GRATUITEMENT (Sprint 1) :
 *   GET    /api/v1/products          → liste paginée
 *   GET    /api/v1/products/{id}     → un produit
 *   POST   /api/v1/products          → créer
 *   PUT    /api/v1/products/{id}     → modifier
 *   DELETE /api/v1/products/{id}     → soft delete
 *   GET    /api/v1/products/count    → total
 *
 * On ajoute les endpoints spécifiques au catalogue.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products", description = "Catalogue produits — CRUD + Recherche + Stock")
public class ProductController extends BaseController<Product, UUID, ProductRequest, ProductResponse> {

    private final ProductService productService;

    @Override
    protected BaseService<Product, UUID, ProductRequest, ProductResponse> getService() {
        return productService;
    }

    // ── Recherche spécialisée ─────────────────────────────────────

    @GetMapping("/reference/{reference}")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @Operation(summary = "Chercher un produit par sa référence")
    public ResponseEntity<ApiResponse<ProductResponse>> findByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.success(productService.findByReference(reference)));
    }

    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @Operation(summary = "Chercher un produit par son code-barres (scan)")
    public ResponseEntity<ApiResponse<ProductResponse>> findByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(ApiResponse.success(productService.findByBarcode(barcode)));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @Operation(summary = "Lister les produits d'une catégorie (paginé)")
    public ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> findByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                productService.findByCategory(categoryId, PageRequest.of(page, size))));
    }

    // ── Alertes stock ─────────────────────────────────────────────

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('STOCK_READ')")
    @Operation(summary = "Produits sous le seuil d'alerte (stock faible)")
    public ResponseEntity<ApiResponse<List<ProductSummaryResponse>>> findLowStock(
            @RequestParam(required = false) UUID siteId) {
        return ResponseEntity.ok(ApiResponse.success(productService.findLowStock(siteId)));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAuthority('STOCK_READ')")
    @Operation(summary = "Produits épuisés (stock = 0)")
    public ResponseEntity<ApiResponse<List<ProductSummaryResponse>>> findOutOfStock(
            @RequestParam(required = false) UUID siteId) {
        return ResponseEntity.ok(ApiResponse.success(productService.findOutOfStock(siteId)));
    }

    // ── Actions ───────────────────────────────────────────────────

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @Operation(summary = "Archiver un produit (stock doit être 0)")
    public ResponseEntity<ApiResponse<ProductResponse>> archive(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(
                productService.archiveProduct(id), "Produit archivé"));
    }
}
