package com.example.sgs_backend.api.customer;


import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.customer.CustomerService;
import com.example.sgs_backend.application.customer.dto.CustomerRequest;
import com.example.sgs_backend.application.customer.dto.CustomerResponse;
import com.example.sgs_backend.domain.customer.Customer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Gestion des clients")
public class CustomerController extends BaseController<Customer, UUID, CustomerRequest, CustomerResponse> {

    private final CustomerService customerService;

    @Override
    protected BaseService<Customer, UUID, CustomerRequest, CustomerResponse> getService() {
        return customerService;
    }
}
