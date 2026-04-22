package com.rares.ecommerce.auth.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AuthProperties authProperties;

    public void sendPasswordResetEmail(String email, String resetLink) {
        send(email, "Reset your password", "Use the following link to reset your password:\n" + resetLink);
    }

    public void sendEmailVerification(String email, String verificationLink) {
        send(email, "Verify your email", "Use the following link to verify your email:\n" + verificationLink);
    }

    private void send(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(authProperties.mailFrom());
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
