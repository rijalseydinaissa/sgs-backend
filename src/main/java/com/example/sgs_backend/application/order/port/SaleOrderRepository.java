package com.example.sgs_backend.application.order.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.order.SaleOrder;

import java.util.*;
public interface SaleOrderRepository extends BaseRepository<SaleOrder, UUID> {
    Optional<SaleOrder> findByReference(String reference);
}
