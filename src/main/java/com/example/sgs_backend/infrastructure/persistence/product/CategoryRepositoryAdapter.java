package com.example.sgs_backend.infrastructure.persistence.product;

import com.example.sgs_backend.application.product.port.ProductCategoryRepository;
import com.example.sgs_backend.domain.product.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository @RequiredArgsConstructor
public class CategoryRepositoryAdapter implements ProductCategoryRepository {

    private final CategoryJpaRepository jpa;

    @Override public ProductCategory save(ProductCategory c)                    { return jpa.save(c); }
    @Override public Optional<ProductCategory> findById(UUID id)               { return jpa.findById(id); }
    @Override public Optional<ProductCategory> findByIdAndDeletedFalse(UUID id){ return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<ProductCategory> findAllByDeletedFalse()             { return jpa.findAllByDeletedFalse(); }
    @Override public Page<ProductCategory> findAllByDeletedFalse(Pageable p)   { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id)                { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse()                                { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id)                                  { jpa.softDeleteById(id); }

    @Override public Optional<ProductCategory> findByCode(String code)         { return jpa.findByCodeAndDeletedFalse(code); }
    @Override public boolean existsByCode(String code)                         { return jpa.existsByCodeAndDeletedFalse(code); }
    @Override public List<ProductCategory> findRootCategories()                { return jpa.findByParentIsNullAndDeletedFalse(); }
    @Override public List<ProductCategory> findByParentId(UUID parentId)       { return jpa.findByParentIdAndDeletedFalse(parentId); }
}
