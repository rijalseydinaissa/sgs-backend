package com.example.sgs_backend.infrastructure.persistence.product;


import com.example.sgs_backend.domain.product.ProductCategory;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** ✅ extends BaseJpaRepository<ProductCategory, UUID> */
@Repository
public interface CategoryJpaRepository extends BaseJpaRepository<ProductCategory, UUID> {
    Optional<ProductCategory> findByCodeAndDeletedFalse(String code);
    boolean existsByCodeAndDeletedFalse(String code);

    // Catégories racines (sans parent)
    List<ProductCategory> findByParentIsNullAndDeletedFalse();
    List<ProductCategory> findByParentIdAndDeletedFalse(UUID parentId);
}
