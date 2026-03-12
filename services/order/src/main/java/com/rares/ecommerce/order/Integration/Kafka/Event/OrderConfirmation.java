package com.rares.ecommerce.order.Integration.Kafka.Event;

import com.rares.ecommerce.order.Integration.Client.Customer.DTO.CustomerResponse;
import com.rares.ecommerce.order.Order.Model.PaymentMethod;
import com.rares.ecommerce.order.Integration.Client.Product.DTO.PurchaseResponse;

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
