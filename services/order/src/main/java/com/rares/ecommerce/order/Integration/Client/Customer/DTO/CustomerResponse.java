package com.rares.ecommerce.order.Integration.Client.Customer.DTO;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
