package com.example.sgs_backend.infrastructure.persistence.customer;

import com.example.sgs_backend.application.customer.port.CustomerRepository;
import com.example.sgs_backend.domain.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository @RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final CustomerJpaRepository jpa;

    @Override public Customer save(Customer c) { return jpa.save(c); }
    @Override public Optional<Customer> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<Customer> findByIdAndDeletedFalse(UUID id) { return jpa.findByIdAndDeletedFalse(id); }
    @Override public List<Customer> findAllByDeletedFalse() { return jpa.findAllByDeletedFalse(); }
    @Override public Page<Customer> findAllByDeletedFalse(Pageable p) { return jpa.findAllByDeletedFalse(p); }
    @Override public boolean existsByIdAndDeletedFalse(UUID id) { return jpa.existsByIdAndDeletedFalse(id); }
    @Override public long countByDeletedFalse() { return jpa.countByDeletedFalse(); }
    @Override public void softDelete(UUID id) { jpa.softDeleteById(id); }
    @Override public Optional<Customer> findByCode(String code) { return jpa.findByCodeAndDeletedFalse(code); }
    @Override public boolean existsByCode(String code) { return jpa.existsByCodeAndDeletedFalse(code); }
}
