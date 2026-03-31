package com.rares.ecommerce.customer.Mapper;

import com.rares.ecommerce.customer.DTO.CustomerRequest;
import com.rares.ecommerce.customer.DTO.CustomerResponse;
import com.rares.ecommerce.customer.model.Customer;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(@Valid CustomerRequest request) {
        if(request == null) return null;
        return Customer.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .address(request.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );

    }
}
