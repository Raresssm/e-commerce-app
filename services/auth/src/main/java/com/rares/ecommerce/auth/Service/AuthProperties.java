package com.rares.ecommerce.auth.Service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.auth")
public record AuthProperties(
        String frontendBaseUrl,
        String mailFrom,
        long resetTokenTtlMinutes,
        long emailVerificationTtlHours
) {
}
