package com.example.sgs_backend.infrastructure.persistence.invoice;

import com.example.sgs_backend.application.invoice.port.InvoiceRepository;
import com.example.sgs_backend.domain.invoice.Invoice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceRepository {
    private final InvoiceJpaRepository jpa;

    @Override public Invoice save(Invoice i) { return jpa.save(i); }
    @Override public Optional<Invoice> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<Invoice> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Invoice> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Invoice> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Optional<Invoice> findByReference(String ref) { return jpa.findByReferenceAndDeletedFalse(ref); }
}
