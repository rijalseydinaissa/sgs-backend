package com.example.sgs_backend.infrastructure.persistence.order;

import com.example.sgs_backend.application.order.port.SaleOrderRepository;
import com.example.sgs_backend.domain.order.SaleOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class SaleOrderRepositoryAdapter implements SaleOrderRepository {
    private final SaleOrderJpaRepository jpa;

    @Override public SaleOrder save(SaleOrder s) { return jpa.save(s); }
    @Override public Optional<SaleOrder> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<SaleOrder> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<SaleOrder> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<SaleOrder> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Optional<SaleOrder> findByReference(String ref) { return jpa.findByReferenceAndDeletedFalse(ref); }
}
