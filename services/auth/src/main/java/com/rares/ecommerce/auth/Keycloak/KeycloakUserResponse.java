package com.rares.ecommerce.auth.Keycloak;

public record KeycloakUserResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        Boolean emailVerified,
        Boolean enabled
) {
}
