package com.rares.ecommerce.payment.Payment.Service;

import com.rares.ecommerce.payment.Notification.NotificationProducer;
import com.rares.ecommerce.payment.Notification.PaymentNotificationRequest;
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
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstname(),
                request.customer().lastname(),
                request.customer().email()
        ));

        return payment.getId();
    }
}
