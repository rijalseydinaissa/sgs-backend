package com.example.sgs_backend.infrastructure.persistence.product;


import com.example.sgs_backend.domain.product.Product;
import com.example.sgs_backend.domain.product.ProductStatus;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseJpaRepository<Product, UUID> (Sprint 1)
 * Hérite : findByIdAndDeletedFalse, findAllByDeletedFalse,
 *          softDeleteById, existsByIdAndDeletedFalse, countByDeletedFalse
 */
@Repository
public interface ProductJpaRepository extends BaseJpaRepository<Product, UUID> {

    Optional<Product> findByReferenceAndDeletedFalse(String reference);
    Optional<Product> findByBarcodeAndDeletedFalse(String barcode);
    boolean existsByReferenceAndDeletedFalse(String reference);
    boolean existsByBarcodeAndDeletedFalse(String barcode);

    Page<Product> findByCategoryIdAndDeletedFalse(UUID categoryId, Pageable pageable);
    Page<Product> findByStatusAndDeletedFalse(ProductStatus status, Pageable pageable);
    Page<Product> findBySiteIdAndDeletedFalse(UUID siteId, Pageable pageable);

    long countByCategoryIdAndDeletedFalse(UUID categoryId);

    // Produits en rupture d'alerte (stock <= seuil min)
    @Query("SELECT p FROM Product p WHERE p.deleted = false " +
           "AND (:siteId IS NULL OR p.siteId = :siteId) " +
           "AND p.currentStock <= p.minimumStock AND p.status != 'ARCHIVED'")
    List<Product> findLowStockProducts(@Param("siteId") UUID siteId);

    // Produits épuisés
    @Query("SELECT p FROM Product p WHERE p.deleted = false " +
           "AND (:siteId IS NULL OR p.siteId = :siteId) " +
           "AND p.currentStock = 0 AND p.status = 'OUT_OF_STOCK'")
    List<Product> findOutOfStockProducts(@Param("siteId") UUID siteId);

    // Recherche texte (nom, référence, barcode)
    @Query("SELECT p FROM Product p WHERE p.deleted = false " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(p.reference) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);
}
