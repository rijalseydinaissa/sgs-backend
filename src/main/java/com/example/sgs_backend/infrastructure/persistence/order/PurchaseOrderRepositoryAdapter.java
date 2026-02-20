package com.example.sgs_backend.infrastructure.persistence.order;

import com.example.sgs_backend.application.order.port.PurchaseOrderRepository;
import com.example.sgs_backend.domain.order.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class PurchaseOrderRepositoryAdapter implements PurchaseOrderRepository {
    private final PurchaseOrderJpaRepository jpa;

    @Override public PurchaseOrder save(PurchaseOrder p) { return jpa.save(p); }
    @Override public Optional<PurchaseOrder> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<PurchaseOrder> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<PurchaseOrder> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<PurchaseOrder> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Optional<PurchaseOrder> findByReference(String ref) { return jpa.findByReferenceAndDeletedFalse(ref); }
}
