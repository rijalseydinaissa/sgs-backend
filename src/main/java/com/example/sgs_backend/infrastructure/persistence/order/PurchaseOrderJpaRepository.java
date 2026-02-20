package com.example.sgs_backend.infrastructure.persistence.order;

import com.example.sgs_backend.domain.order.PurchaseOrder;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PurchaseOrderJpaRepository extends BaseJpaRepository<PurchaseOrder, UUID> {
    Optional<PurchaseOrder> findByReferenceAndDeletedFalse(String reference);
}
