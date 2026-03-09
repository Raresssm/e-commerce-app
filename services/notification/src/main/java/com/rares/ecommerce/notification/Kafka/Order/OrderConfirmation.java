package com.rares.ecommerce.notification.Kafka.Order;

import com.rares.ecommerce.notification.Kafka.Payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,
        List<Product> products
) {
}
