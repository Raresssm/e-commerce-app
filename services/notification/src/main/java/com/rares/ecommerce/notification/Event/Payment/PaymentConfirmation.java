package com.rares.ecommerce.notification.Event.Payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String id,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
