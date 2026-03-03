package com.rares.ecommerce.customer.Service;

import com.rares.ecommerce.customer.DTO.CustomerResponse;
import com.rares.ecommerce.customer.Repository.CustomerRepository;
import com.rares.ecommerce.customer.DTO.CustomerRequest;
import com.rares.ecommerce.customer.Exceptions.CustomerNotFoundException;
import com.rares.ecommerce.customer.Mapper.CustomerMapper;
import com.rares.ecommerce.customer.model.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(@Valid CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var customer = repository.findById(request.id())
                .orElseThrow  (()-> new CustomerNotFoundException(
                        format("Cannot update customer:: No customer found with the provide ID:: %s", request.id())
                ));
        mergerCustomer(customer, request);
        repository.save(customer);
    }

    private void mergerCustomer(Customer customer, @Valid CustomerRequest request) {
        if(StringUtils.isNotBlank((request.firstname())))
            customer.setFirstname(request.firstname());

        if(StringUtils.isNotBlank((request.lastname())))
            customer.setFirstname(request.lastname());

        if(StringUtils.isNotBlank((request.email())))
            customer.setFirstname(request.email());

        if(request.address() != null)
            customer.setAddress(request.address());
    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return repository.existsById(customerId);
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(()-> new CustomerNotFoundException(
                        format("No customer found with ID:: %s", customerId)
                ));
    }

    public void deleteById(String customerId) {
        repository.deleteById(customerId);
    }
}
