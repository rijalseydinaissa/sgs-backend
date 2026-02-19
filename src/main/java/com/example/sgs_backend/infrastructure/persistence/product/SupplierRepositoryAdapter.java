package com.example.sgs_backend.infrastructure.persistence.product;


import com.example.sgs_backend.application.product.port.SupplierRepository;
import com.example.sgs_backend.domain.product.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository @RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepository {

    private final SupplierJpaRepository jpa;

    @Override public Supplier save(Supplier s)                          { return jpa.save(s); }
    @Override public Optional<Supplier> findById(UUID id)              { return jpa.findById(id); }
    @Override public Optional<Supplier> findByIdAndDeletedFalse(UUID id){ return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Supplier> findAllByDeletedFalse()            { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Supplier> findAllByDeletedFalse(Pageable p)  { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id)        { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse()                        { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id)                          { jpa.softDeleteById(id); }

    @Override public Optional<Supplier> findByCode(String code)        { return jpa.findByCodeAndDeletedFalse(code); }
    @Override public boolean existsByCode(String code)                 { return jpa.existsByCodeAndDeletedFalse(code); }
    @Override public boolean existsByEmail(String email)               { return jpa.existsByEmailAndDeletedFalse(email); }
}
