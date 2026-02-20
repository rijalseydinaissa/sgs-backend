package com.example.sgs_backend.infrastructure.persistence.customer;

import com.example.sgs_backend.domain.customer.Customer;
import com.example.sgs_backend.infrastructure.persistence.common.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CustomerJpaRepository extends BaseJpaRepository<Customer, UUID> {
    Optional<Customer> findByCodeAndDeletedFalse(String code);
    boolean existsByCodeAndDeletedFalse(String code);
}
