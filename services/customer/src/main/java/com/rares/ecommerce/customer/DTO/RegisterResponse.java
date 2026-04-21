package com.rares.ecommerce.customer.DTO;

public record RegisterResponse(
        String customerId,
        String keycloakUserId
) {
}
