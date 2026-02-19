package com.example.sgs_backend.api.product;

import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.product.SupplierService;
import com.example.sgs_backend.application.product.dto.SupplierRequest;
import com.example.sgs_backend.application.product.dto.SupplierResponse;
import com.example.sgs_backend.domain.product.Supplier;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** ✅ extends BaseController<Supplier, UUID, SupplierRequest, SupplierResponse> */
@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Suppliers", description = "Gestion des fournisseurs")
public class SupplierController extends BaseController<Supplier, UUID, SupplierRequest, SupplierResponse> {

    private final SupplierService supplierService;

    @Override
    protected BaseService<Supplier, UUID, SupplierRequest, SupplierResponse> getService() {
        return supplierService;
    }
    // Les 6 endpoints CRUD sont hérités de BaseController ✅
}
