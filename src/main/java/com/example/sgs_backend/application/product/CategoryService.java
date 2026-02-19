package com.example.sgs_backend.application.product;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.product.dto.CategoryRequest;
import com.example.sgs_backend.application.product.dto.CategoryResponse;
import com.example.sgs_backend.application.product.port.ProductCategoryRepository;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.product.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/** ✅ extends BaseService<ProductCategory, UUID, CategoryRequest, CategoryResponse> */
@Service
public class CategoryService extends BaseService<ProductCategory, UUID, CategoryRequest, CategoryResponse> {

    private final ProductCategoryRepository categoryRepository;

    public CategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override protected BaseRepository<ProductCategory, UUID> getRepository() { return categoryRepository; }
    @Override protected String getEntityName() { return "ProductCategory"; }

    @Override
    protected ProductCategory toEntity(CategoryRequest req) {
        if (categoryRepository.existsByCode(req.code()))
            throw new DuplicateResourceException("ProductCategory", "code", req.code());

        ProductCategory parent = null;
        if (req.parentId() != null)
            parent = categoryRepository.findByIdAndDeletedFalse(req.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", req.parentId()));

        var cat = new ProductCategory(req.code(), req.name(), parent);
        cat.setDescription(req.description());
        cat.setIconUrl(req.iconUrl());
        return cat;
    }

    @Override
    protected CategoryResponse toResponse(ProductCategory c) {
        List<CategoryResponse> children = c.getChildren() == null ? List.of() :
                c.getChildren().stream()
                        .filter(ch -> !ch.isDeleted())
                        .map(this::toResponse)
                        .toList();
        return new CategoryResponse(
                c.getId(), c.getCode(), c.getName(), c.getDescription(), c.getIconUrl(),
                c.getParent() != null ? c.getParent().getId() : null,
                c.getParent() != null ? c.getParent().getName() : null,
                c.getFullPath(), c.getDepth(), 0L, children
        );
    }

    @Override
    protected void updateEntity(ProductCategory cat, CategoryRequest req) {
        if (!cat.getCode().equals(req.code()) && categoryRepository.existsByCode(req.code()))
            throw new DuplicateResourceException("ProductCategory", "code", req.code());

        ProductCategory parent = null;
        if (req.parentId() != null)
            parent = categoryRepository.findByIdAndDeletedFalse(req.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", req.parentId()));

        cat.setCode(req.code());
        cat.setName(req.name());
        cat.setDescription(req.description());
        cat.setIconUrl(req.iconUrl());
        cat.setParent(parent);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findRootCategories() {
        return categoryRepository.findRootCategories().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findChildren(UUID parentId) {
        return categoryRepository.findByParentId(parentId).stream().map(this::toResponse).toList();
    }
}
