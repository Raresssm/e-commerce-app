package com.rares.ecommerce.order.Clients.Payment.DTO;

import com.rares.ecommerce.order.Clients.Customer.DTO.CustomerResponse;
import com.rares.ecommerce.order.Order.Model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
