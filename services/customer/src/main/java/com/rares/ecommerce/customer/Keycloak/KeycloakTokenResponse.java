package com.rares.ecommerce.customer.Keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakTokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}
