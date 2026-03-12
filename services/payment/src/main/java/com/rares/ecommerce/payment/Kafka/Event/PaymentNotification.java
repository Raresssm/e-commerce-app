package com.rares.ecommerce.payment.Kafka.Event;

import com.rares.ecommerce.payment.Payment.Model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotification(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
