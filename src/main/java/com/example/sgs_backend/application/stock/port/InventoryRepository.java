package com.example.sgs_backend.application.stock.port;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.stock.Inventory;

import java.util.*;

/** ✅ extends BaseRepository<Inventory, UUID> */
public interface InventoryRepository extends BaseRepository<Inventory, UUID> {
    Optional<Inventory> findByReference(String reference);
    boolean existsByReference(String reference);
    List<Inventory> findBySiteIdAndDeletedFalse(UUID siteId);
}
