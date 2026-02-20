package com.example.sgs_backend.application.customer;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.customer.dto.CustomerRequest;
import com.example.sgs_backend.application.customer.dto.CustomerResponse;
import com.example.sgs_backend.application.customer.port.CustomerRepository;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.valueobject.Address;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.customer.Customer;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CustomerService extends BaseService<Customer, UUID, CustomerRequest, CustomerResponse> {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override protected BaseRepository<Customer, UUID> getRepository() { return customerRepository; }
    @Override protected String getEntityName() { return "Customer"; }

    @Override
    protected Customer toEntity(CustomerRequest req) {
        if (customerRepository.existsByCode(req.code()))
            throw new DuplicateResourceException("Customer", "code", req.code());

        Address address = req.addressCity() != null
                ? Address.of(req.addressStreet(), req.addressCity(), null, null, req.addressCountry())
                : null;

        return Customer.builder()
                .code(req.code()).name(req.name())
                .customerType(req.customerType())
                .phone(req.phone()).email(req.email())
                .address(address)
                .accountBalance(Money.zero("XOF"))
                .active(true).build();
    }

    @Override
    protected CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getId(), c.getCode(), c.getName(), c.getCustomerType(),
                c.getPhone(), c.getEmail(),
                c.getAddress() != null ? c.getAddress().getCity() : null,
                c.getAddress() != null ? c.getAddress().getCountry() : null,
                c.getAccountBalance() != null ? c.getAccountBalance().getAmount() : null,
                c.getAccountBalance() != null ? c.getAccountBalance().getCurrency() : null,
                c.isActive(), c.getCreatedAt()
        );
    }

    @Override
    protected void updateEntity(Customer customer, CustomerRequest req) {
        customer.setName(req.name());
        customer.setPhone(req.phone());
        customer.setEmail(req.email());
    }
}
