package com.example.sgs_backend.application.product.port;



import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.product.Supplier;

import java.util.Optional;
import java.util.UUID;

/** ✅ extends BaseRepository<Supplier, UUID> */
public interface SupplierRepository extends BaseRepository<Supplier, UUID> {
    Optional<Supplier> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
}
