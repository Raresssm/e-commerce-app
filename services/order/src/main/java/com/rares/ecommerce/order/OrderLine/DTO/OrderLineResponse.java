package com.rares.ecommerce.order.OrderLine.DTO;

public record OrderLineResponse(
        Integer id,
        Integer productId,
        Integer quantity
) {
}
