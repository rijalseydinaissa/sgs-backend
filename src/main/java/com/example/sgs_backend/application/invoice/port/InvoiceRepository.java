package com.example.sgs_backend.application.invoice.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.invoice.Invoice;

import java.util.*;
public interface InvoiceRepository extends BaseRepository<Invoice, UUID> {
    Optional<Invoice> findByReference(String reference);
}
