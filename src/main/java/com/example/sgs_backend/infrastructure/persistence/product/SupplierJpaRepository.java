package com.example.sgs_backend.infrastructure.persistence.product;


import com.example.sgs_backend.domain.product.Supplier;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/** ✅ extends BaseJpaRepository<Supplier, UUID> */
@Repository
public interface SupplierJpaRepository extends BaseJpaRepository<Supplier, UUID> {
    Optional<Supplier> findByCodeAndDeletedFalse(String code);
    boolean existsByCodeAndDeletedFalse(String code);
    boolean existsByEmailAndDeletedFalse(String email);
}
