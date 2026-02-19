package com.example.sgs_backend.application.product;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.product.dto.SupplierRequest;
import com.example.sgs_backend.application.product.dto.SupplierResponse;
import com.example.sgs_backend.application.product.port.SupplierRepository;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.valueobject.Address;
import com.example.sgs_backend.domain.product.Supplier;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** ✅ extends BaseService<Supplier, UUID, SupplierRequest, SupplierResponse> */
@Service
public class SupplierService extends BaseService<Supplier, UUID, SupplierRequest, SupplierResponse> {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override protected BaseRepository<Supplier, UUID> getRepository() { return supplierRepository; }
    @Override protected String getEntityName() { return "Supplier"; }

    @Override
    protected Supplier toEntity(SupplierRequest req) {
        if (supplierRepository.existsByCode(req.code()))
            throw new DuplicateResourceException("Supplier", "code", req.code());
        if (req.email() != null && supplierRepository.existsByEmail(req.email()))
            throw new DuplicateResourceException("Supplier", "email", req.email());

        Address address = null;
        if (req.addressCity() != null && req.addressCountry() != null)
            address = Address.of(req.addressStreet(), req.addressCity(),
                                 req.addressRegion(), null, req.addressCountry());

        return Supplier.builder()
                .code(req.code()).name(req.name())
                .phone(req.phone()).email(req.email())
                .contactPerson(req.contactPerson()).taxNumber(req.taxNumber())
                .address(address)
                .paymentTermsDays(req.paymentTermsDays() != null ? req.paymentTermsDays() : 30)
                .active(true).build();
    }

    @Override
    protected SupplierResponse toResponse(Supplier s) {
        return new SupplierResponse(
                s.getId(), s.getCode(), s.getName(),
                s.getPhone(), s.getEmail(), s.getContactPerson(),
                s.getTaxNumber(),
                s.getAddress() != null ? s.getAddress().getCity() : null,
                s.getAddress() != null ? s.getAddress().getCountry() : null,
                s.getPaymentTermsDays(), s.getAverageRating(),
                s.isActive(), s.getCreatedAt()
        );
    }

    @Override
    protected void updateEntity(Supplier s, SupplierRequest req) {
        Address address = null;
        if (req.addressCity() != null && req.addressCountry() != null)
            address = Address.of(req.addressStreet(), req.addressCity(),
                                 req.addressRegion(), null, req.addressCountry());

        s.setName(req.name()); s.setPhone(req.phone());
        s.setEmail(req.email()); s.setContactPerson(req.contactPerson());
        s.setTaxNumber(req.taxNumber()); s.setAddress(address);
        if (req.paymentTermsDays() != null) s.setPaymentTermsDays(req.paymentTermsDays());
    }
}
