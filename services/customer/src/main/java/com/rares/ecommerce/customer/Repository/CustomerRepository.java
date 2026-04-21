package com.rares.ecommerce.customer.Repository;

import com.rares.ecommerce.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByKeycloakId(String keycloakId);
    boolean existsByEmail(String email);

}
