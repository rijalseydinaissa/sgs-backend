package com.example.sgs_backend.application.product.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.product.Product;
import com.example.sgs_backend.domain.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ✅ extends BaseRepository<Product, UUID> (Sprint 1)
 * Hérite : save, findById, findByIdAndDeletedFalse,
 *          findAllByDeletedFalse(Pageable), softDelete,
 *          existsByIdAndDeletedFalse, countByDeletedFalse
 * On ajoute UNIQUEMENT les méthodes spécifiques au domaine Product.
 */
public interface ProductRepository extends BaseRepository<Product, UUID> {
    Optional<Product> findByReference(String reference);
    Optional<Product> findByBarcode(String barcode);
    boolean existsByReference(String reference);
    boolean existsByBarcode(String barcode);
    Page<Product> findByCategoryIdAndDeletedFalse(UUID categoryId, Pageable pageable);
    Page<Product> findByStatusAndDeletedFalse(ProductStatus status, Pageable pageable);
    Page<Product> findBySiteIdAndDeletedFalse(UUID siteId, Pageable pageable);
    List<Product> findLowStockProducts(UUID siteId);   // currentStock <= minimumStock
    List<Product> findOutOfStockProducts(UUID siteId);
    long countByCategoryIdAndDeletedFalse(UUID categoryId);
}
