package com.rares.ecommerce.order.Kafka;

import com.rares.ecommerce.order.Clients.Customer.DTO.CustomerResponse;
import com.rares.ecommerce.order.Order.Model.PaymentMethod;
import com.rares.ecommerce.order.Clients.Product.DTO.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
