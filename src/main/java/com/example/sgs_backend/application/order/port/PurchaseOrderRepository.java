package com.example.sgs_backend.application.order.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.order.PurchaseOrder;

import java.util.*;
public interface PurchaseOrderRepository extends BaseRepository<PurchaseOrder, UUID> {
    Optional<PurchaseOrder> findByReference(String reference);
}
