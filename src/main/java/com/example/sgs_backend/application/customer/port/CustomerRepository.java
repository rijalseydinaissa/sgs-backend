package com.example.sgs_backend.application.customer.port;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.domain.customer.Customer;

import java.util.*;
public interface CustomerRepository extends BaseRepository<Customer, UUID> {
    Optional<Customer> findByCode(String code);
    boolean existsByCode(String code);
}
