package com.rares.ecommerce.customer.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {
    private String city;
    private String street;
    private String housenumber;
    private String zipcode;
}
