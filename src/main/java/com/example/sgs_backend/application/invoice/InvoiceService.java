package com.example.sgs_backend.application.invoice;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.invoice.port.InvoiceRepository;
import com.example.sgs_backend.domain.invoice.Invoice;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InvoiceService extends BaseService<Invoice, UUID, Object, Object> {

    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override protected BaseRepository<Invoice, UUID> getRepository() { return repository; }
    @Override protected String getEntityName() { return "Invoice"; }
    @Override protected Invoice toEntity(Object req) { return null; }
    @Override protected Object toResponse(Invoice entity) { return null; }
    @Override protected void updateEntity(Invoice entity, Object req) {}
}
