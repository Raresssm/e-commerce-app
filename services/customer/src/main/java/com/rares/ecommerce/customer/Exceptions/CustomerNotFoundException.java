package com.rares.ecommerce.customer.Exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class CustomerNotFoundException extends RuntimeException {
    private String message;

    public CustomerNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
