package com.example.sgs_backend.api.invoice;

import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.invoice.InvoiceService;
import com.example.sgs_backend.domain.invoice.Invoice;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Factures clients")
public class InvoiceController extends BaseController<Invoice, UUID, Object, Object> {

    private final InvoiceService service;

    @Override
    protected BaseService<Invoice, UUID, Object, Object> getService() {
        return service;
    }
}
