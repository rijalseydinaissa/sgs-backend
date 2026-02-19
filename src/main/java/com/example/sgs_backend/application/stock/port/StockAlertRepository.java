package com.example.sgs_backend.application.stock.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.stock.StockAlert;

import java.util.*;

/** ✅ extends BaseRepository<StockAlert, UUID> */
public interface StockAlertRepository extends BaseRepository<StockAlert, UUID> {
    List<StockAlert> findActiveAlertsByProduct(UUID productId);
    List<StockAlert> findActiveAlerts();
    long countActiveAlerts();
}
