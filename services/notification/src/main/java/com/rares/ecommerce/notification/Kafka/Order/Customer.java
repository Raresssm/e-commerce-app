package com.rares.ecommerce.notification.Kafka.Order;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
