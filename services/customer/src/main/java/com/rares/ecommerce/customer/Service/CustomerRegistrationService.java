package com.rares.ecommerce.customer.Service;

import com.rares.ecommerce.customer.DTO.RegisterRequest;
import com.rares.ecommerce.customer.DTO.RegisterResponse;
import com.rares.ecommerce.customer.Exceptions.CustomerAlreadyExistsException;
import com.rares.ecommerce.customer.Keycloak.KeycloakAdminService;
import com.rares.ecommerce.customer.Mapper.CustomerMapper;
import com.rares.ecommerce.customer.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final KeycloakAdminService keycloakAdminService;

    public RegisterResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(
                    "A customer already exists with email:: " + request.email()
            );
        }

        String keycloakUserId = keycloakAdminService.createUser(request);
        var customer = repository.save(mapper.toCustomer(request, keycloakUserId));

        return new RegisterResponse(customer.getId(), keycloakUserId);
    }
}
