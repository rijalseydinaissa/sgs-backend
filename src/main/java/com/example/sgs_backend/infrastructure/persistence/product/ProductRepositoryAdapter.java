package com.example.sgs_backend.infrastructure.persistence.product;

import com.example.sgs_backend.application.product.port.ProductRepository;
import com.example.sgs_backend.domain.product.Product;
import com.example.sgs_backend.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur : implémente ProductRepository (Application)
 * en déléguant à ProductJpaRepository (Infrastructure).
 *
 * ✅ Pas de mapper Entity ↔ JpaEntity — Product extends BaseEntity,
 *    JPA l'utilise directement. Zéro duplication !
 */
@Repository @RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpa;

    // ── Méthodes de BaseRepository ────────────────────────────────z
    @Override public Product save(Product p)                            { return jpa.save(p); }
    @Override public Optional<Product> findById(UUID id)               { return jpa.findById(id); }
    @Override public Optional<Product> findByIdAndDeletedFalse(UUID id){ return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Product> findAllByDeletedFalse()             { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Product> findAllByDeletedFalse(Pageable p)   { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id)        { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse()                        { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id)                          { jpa.softDeleteById(id); }

    // ── Méthodes spécifiques à ProductRepository ──────────────────
    @Override public Optional<Product> findByReference(String ref)     { return jpa.findByReferenceAndDeletedFalse(ref); }
    @Override public Optional<Product> findByBarcode(String bc)        { return jpa.findByBarcodeAndDeletedFalse(bc); }
    @Override public boolean existsByReference(String ref)             { return jpa.existsByReferenceAndDeletedFalse(ref); }
    @Override public boolean existsByBarcode(String bc)                { return jpa.existsByBarcodeAndDeletedFalse(bc); }
    @Override public Page<Product> findByCategoryIdAndDeletedFalse(UUID cId, Pageable p) { return jpa.findByCategoryIdAndDeletedFalse(cId, p); }
    @Override public Page<Product> findByStatusAndDeletedFalse(ProductStatus s, Pageable p) { return jpa.findByStatusAndDeletedFalse(s, p); }
    @Override public Page<Product> findBySiteIdAndDeletedFalse(UUID sId, Pageable p) { return jpa.findBySiteIdAndDeletedFalse(sId, p); }
    @Override public List<Product> findLowStockProducts(UUID siteId)   { return jpa.findLowStockProducts(siteId); }
    @Override public List<Product> findOutOfStockProducts(UUID siteId) { return jpa.findOutOfStockProducts(siteId); }
    @Override public long countByCategoryIdAndDeletedFalse(UUID cId)   { return jpa.countByCategoryIdAndDeletedFalse(cId); }
}
