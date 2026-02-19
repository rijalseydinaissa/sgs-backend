package com.example.sgs_backend.api.product;


import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.product.CategoryService;
import com.example.sgs_backend.application.product.dto.CategoryRequest;
import com.example.sgs_backend.application.product.dto.CategoryResponse;
import com.example.sgs_backend.domain.product.ProductCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** ✅ extends BaseController<ProductCategory, UUID, CategoryRequest, CategoryResponse> */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categories", description = "Catégories produits arborescentes")
public class CategoryController extends BaseController<ProductCategory, UUID, CategoryRequest, CategoryResponse> {

    private final CategoryService categoryService;

    @Override
    protected BaseService<ProductCategory, UUID, CategoryRequest, CategoryResponse> getService() {
        return categoryService;
    }

    @GetMapping("/roots")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @Operation(summary = "Lister les catégories racines (niveau 0)")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRoots() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findRootCategories()));
    }

    @GetMapping("/{parentId}/children")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @Operation(summary = "Lister les sous-catégories d'une catégorie")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildren(@PathVariable UUID parentId) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findChildren(parentId)));
    }
}
