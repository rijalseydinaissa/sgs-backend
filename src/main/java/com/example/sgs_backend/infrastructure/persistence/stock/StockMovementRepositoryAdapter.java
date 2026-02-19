package com.example.sgs_backend.infrastructure.persistence.stock;


import com.example.sgs_backend.application.stock.port.StockMovementRepository;
import com.example.sgs_backend.domain.product.MovementType;
import com.example.sgs_backend.domain.stock.StockMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;

@Repository @RequiredArgsConstructor
public class StockMovementRepositoryAdapter implements StockMovementRepository {

    private final StockMovementJpaRepository jpa;

    @Override public StockMovement save(StockMovement m) { return jpa.save(m); }
    @Override public Optional<StockMovement> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<StockMovement> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<StockMovement> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<StockMovement> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Page<StockMovement> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable) { return jpa.findByProductIdAndDeletedFalse(productId, pageable); }
    @Override public Page<StockMovement> findByMovementTypeAndDeletedFalse(MovementType type, Pageable p) { return jpa.findByMovementTypeAndDeletedFalse(type, p); }
    @Override public List<StockMovement> findByProductIdAndMovementDateBetween(UUID p, LocalDateTime s, LocalDateTime e) { return jpa.findByProductIdAndMovementDateBetween(p, s, e); }
    @Override public List<StockMovement> findByFromSiteId(UUID siteId) { return List.of(); }
    @Override public List<StockMovement> findByToSiteId(UUID siteId) { return List.of(); }
}
