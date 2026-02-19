package com.example.sgs_backend.application.stock;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.product.port.ProductRepository;
import com.example.sgs_backend.application.stock.dto.StockMovementRequest;
import com.example.sgs_backend.application.stock.dto.StockMovementResponse;
import com.example.sgs_backend.application.stock.port.StockMovementRepository;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.Product;
import com.example.sgs_backend.domain.stock.StockMovement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ✅ extends BaseService<StockMovement, UUID, StockMovementRequest, StockMovementResponse>
 * Use Case stock complet : entrées, sorties, transferts, ajustements.
 */
@Service @Slf4j
public class StockService extends BaseService<StockMovement, UUID, StockMovementRequest, StockMovementResponse> {

    private final StockMovementRepository movementRepository;
    private final ProductRepository       productRepository;

    public StockService(StockMovementRepository movementRepository,
                        ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository  = productRepository;
    }

    @Override protected BaseRepository<StockMovement, UUID> getRepository() { return movementRepository; }
    @Override protected String getEntityName() { return "StockMovement"; }

    @Override
    protected StockMovement toEntity(StockMovementRequest req) {
        Product product = productRepository.findByIdAndDeletedFalse(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", req.productId()));

        Quantity quantity = Quantity.of(req.quantityValue(), req.quantityUnit());

        return StockMovement.builder()
                .productId(req.productId())
                .movementType(req.movementType())
                .quantity(quantity)
                .movementDate(req.movementDate() != null ? req.movementDate() : LocalDateTime.now())
                .reference(req.reference())
                .notes(req.notes())
                .fromSiteId(req.fromSiteId())
                .toSiteId(req.toSiteId())
                .stockBefore(product.getCurrentStock().getValue())
                .stockAfter(product.getCurrentStock().getValue()) // Sera mis à jour après
                .build();
    }

    @Override
    protected StockMovementResponse toResponse(StockMovement m) {
        return new StockMovementResponse(
                m.getId(), m.getProductId(), null, m.getMovementType(),
                m.getQuantity().getValue(), m.getQuantity().getUnit(),
                m.getMovementDate(), m.getReference(), m.getNotes(),
                m.getFromSiteId(), m.getToSiteId(),
                m.getStockBefore(), m.getStockAfter(),
                m.getCreatedAt(), m.getCreatedBy()
        );
    }

    @Override
    protected void updateEntity(StockMovement movement, StockMovementRequest req) {
        // Les mouvements historiques ne devraient pas être modifiés
        throw new UnsupportedOperationException("Les mouvements de stock ne peuvent pas être modifiés");
    }

    // ═══════ Use Cases spécifiques ═══════

    @Transactional
    @PreAuthorize("hasAuthority('STOCK_ENTRY')")
    public StockMovementResponse recordEntry(StockMovementRequest req, UUID userId) {
        StockMovement movement = toEntity(req);
        Product product = productRepository.findByIdAndDeletedFalse(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", req.productId()));

        // Appliquer le mouvement
        product.addStock(movement.getQuantity());
        movement.setStockAfter(product.getCurrentStock().getValue());
        movement.setUserId(userId);

        productRepository.save(product);
        StockMovement saved = movementRepository.save(movement);
        log.info("Entrée stock : {} → +{}", product.getReference(), movement.getQuantity());
        return toResponse(saved);
    }

    @Transactional
    @PreAuthorize("hasAuthority('STOCK_EXIT')")
    public StockMovementResponse recordExit(StockMovementRequest req, UUID userId) {
        StockMovement movement = toEntity(req);
        Product product = productRepository.findByIdAndDeletedFalse(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", req.productId()));

        product.removeStock(movement.getQuantity()); // Lève exception si insuffisant
        movement.setStockAfter(product.getCurrentStock().getValue());
        movement.setUserId(userId);

        productRepository.save(product);
        StockMovement saved = movementRepository.save(movement);
        log.info("Sortie stock : {} → -{}", product.getReference(), movement.getQuantity());
        return toResponse(saved);
    }
}
