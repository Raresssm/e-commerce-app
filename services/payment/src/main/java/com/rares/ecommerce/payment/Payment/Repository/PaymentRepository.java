package com.rares.ecommerce.payment.Payment.Repository;

import com.rares.ecommerce.payment.Payment.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
