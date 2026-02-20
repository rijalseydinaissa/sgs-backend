package com.example.sgs_backend.infrastructure.persistence.order;

import com.example.sgs_backend.domain.order.SaleOrder;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SaleOrderJpaRepository extends BaseJpaRepository<SaleOrder, UUID> {
    Optional<SaleOrder> findByReferenceAndDeletedFalse(String reference);
}
