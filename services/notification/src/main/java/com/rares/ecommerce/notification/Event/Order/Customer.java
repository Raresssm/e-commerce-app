package com.rares.ecommerce.notification.Event.Order;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
