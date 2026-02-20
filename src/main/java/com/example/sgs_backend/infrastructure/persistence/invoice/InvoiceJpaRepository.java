package com.example.sgs_backend.infrastructure.persistence.invoice;

import com.example.sgs_backend.domain.invoice.Invoice;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface InvoiceJpaRepository extends BaseJpaRepository<Invoice, UUID> {
    Optional<Invoice> findByReferenceAndDeletedFalse(String reference);
}
