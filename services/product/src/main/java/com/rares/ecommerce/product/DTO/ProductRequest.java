package com.rares.ecommerce.product.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotNull(message = "Product name required!")
        String name,
        @NotNull(message = "Product description required!")
        String description,
        @Positive(message = "Product available quantity should be positive !")
        double availableQuantity,
        @Positive(message = "Product price should be positive !")
        BigDecimal price,
        @NotNull(message = "Product category required!")
        Integer categoryId
) {
}
