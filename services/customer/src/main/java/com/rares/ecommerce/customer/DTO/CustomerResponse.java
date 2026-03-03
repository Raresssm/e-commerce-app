package com.rares.ecommerce.customer.DTO;

import com.rares.ecommerce.customer.model.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}
