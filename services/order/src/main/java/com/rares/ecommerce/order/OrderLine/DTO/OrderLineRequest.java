package com.rares.ecommerce.order.OrderLine.DTO;

public record OrderLineRequest(
        Integer id,
        Integer orderId,
        Integer productId,
        double quantity) {
}
