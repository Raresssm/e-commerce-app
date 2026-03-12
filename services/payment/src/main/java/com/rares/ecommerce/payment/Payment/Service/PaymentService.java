package com.rares.ecommerce.payment.Payment.Service;

import com.rares.ecommerce.payment.Kafka.Producer.NotificationProducer;
import com.rares.ecommerce.payment.Kafka.Event.PaymentNotification;
import com.rares.ecommerce.payment.Payment.DTO.PaymentRequest;
import com.rares.ecommerce.payment.Payment.Mapper.PaymentMapper;
import com.rares.ecommerce.payment.Payment.Repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(@Valid PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(new PaymentNotification(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customerResponse().firstname(),
                request.customerResponse().lastname(),
                request.customerResponse().email()
        ));
        return payment.getId();
    }
}
