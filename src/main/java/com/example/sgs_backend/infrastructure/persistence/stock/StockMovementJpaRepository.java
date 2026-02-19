package com.example.sgs_backend.infrastructure.persistence.stock;


import com.example.sgs_backend.domain.product.MovementType;
import com.example.sgs_backend.domain.stock.StockMovement;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;

/** ✅ extends BaseJpaRepository<StockMovement, UUID> */
@Repository
public interface StockMovementJpaRepository extends BaseJpaRepository<StockMovement, UUID> {
    Page<StockMovement> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable);
    Page<StockMovement> findByMovementTypeAndDeletedFalse(MovementType type, Pageable pageable);
    List<StockMovement> findByProductIdAndMovementDateBetween(UUID productId, LocalDateTime start, LocalDateTime end);
}
