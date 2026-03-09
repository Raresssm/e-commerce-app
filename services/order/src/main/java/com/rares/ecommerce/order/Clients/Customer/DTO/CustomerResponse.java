package com.rares.ecommerce.order.Clients.Customer.DTO;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
