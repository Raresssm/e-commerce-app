package com.rares.ecommerce.order.Integration.Client.Payment;

import com.rares.ecommerce.order.Integration.Client.Payment.DTO.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {
    @PostMapping
    Integer requestPayment(@RequestBody PaymentRequest paymentRequest);
}
