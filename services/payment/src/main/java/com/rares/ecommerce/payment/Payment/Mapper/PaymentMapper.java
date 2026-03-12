package com.rares.ecommerce.payment.Payment.Mapper;

import com.rares.ecommerce.payment.Payment.DTO.PaymentRequest;
import com.rares.ecommerce.payment.Payment.Model.Payment;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    //todo: add customerResponse field
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
                .id(request.id())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .build();
    }
}
