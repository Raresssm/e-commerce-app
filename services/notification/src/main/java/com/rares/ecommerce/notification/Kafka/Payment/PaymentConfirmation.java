package com.rares.ecommerce.notification.Kafka.Payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String id,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
