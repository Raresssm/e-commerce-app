package com.rares.ecommerce.order.Order.DTO;

import com.rares.ecommerce.order.Order.Model.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerId
) {
}
