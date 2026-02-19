package com.example.sgs_backend.application.product.port;



import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.product.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** ✅ extends BaseRepository<ProductCategory, UUID> */
public interface ProductCategoryRepository extends BaseRepository<ProductCategory, UUID> {
    Optional<ProductCategory> findByCode(String code);
    boolean existsByCode(String code);
    List<ProductCategory> findRootCategories();        // parent == null
    List<ProductCategory> findByParentId(UUID parentId);
}
