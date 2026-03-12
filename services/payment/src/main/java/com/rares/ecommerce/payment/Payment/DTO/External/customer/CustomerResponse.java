package com.rares.ecommerce.payment.Payment.DTO.External.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CustomerResponse(
        String id,
        @NotNull(message = "Firstname is required!")
        String firstname,
        @NotNull(message = "Lastname is required!")
        String lastname,
        @NotNull(message = "Email is required!")
        @Email(message = "The customerResponse email is not correctly formatted!")
        String email
) {
}
