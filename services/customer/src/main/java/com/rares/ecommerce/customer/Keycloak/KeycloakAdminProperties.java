package com.rares.ecommerce.customer.Keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.keycloak")
public record KeycloakAdminProperties(
        String serverUrl,
        String realm,
        String adminRealm,
        String adminClientId,
        String adminUsername,
        String adminPassword
) {
}
