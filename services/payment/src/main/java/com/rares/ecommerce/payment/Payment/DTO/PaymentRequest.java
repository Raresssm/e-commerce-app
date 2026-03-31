package com.rares.ecommerce.payment.Payment.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rares.ecommerce.payment.Payment.DTO.External.customer.CustomerResponse;
import com.rares.ecommerce.payment.Payment.Model.PaymentMethod;

import java.math.BigDecimal;

//todo: add validation
public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        @JsonProperty("customer")
        CustomerResponse customerResponse
) {
}
