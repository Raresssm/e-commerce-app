package com.rares.ecommerce.customer.DTO;

import com.rares.ecommerce.customer.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
    String id,
    @NotNull(message = "First name required")
    String firstname,
    @NotNull(message = "Last name required")
    String lastname,
    @NotNull(message = "Email required")
    @Email(message= "Email should be valid")
    String email,
    Address address
){

}
