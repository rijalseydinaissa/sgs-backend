package com.example.sgs_backend.application.stock.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.product.MovementType;
import com.example.sgs_backend.domain.stock.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.*;

/** ✅ extends BaseRepository<StockMovement, UUID> */
public interface StockMovementRepository extends BaseRepository<StockMovement, UUID> {
    Page<StockMovement> findByProductIdAndDeletedFalse(UUID productId, Pageable pageable);
    Page<StockMovement> findByMovementTypeAndDeletedFalse(MovementType type, Pageable pageable);
    List<StockMovement> findByProductIdAndMovementDateBetween(
            UUID productId, LocalDateTime start, LocalDateTime end);
    List<StockMovement> findByFromSiteId(UUID siteId);
    List<StockMovement> findByToSiteId(UUID siteId);
}
