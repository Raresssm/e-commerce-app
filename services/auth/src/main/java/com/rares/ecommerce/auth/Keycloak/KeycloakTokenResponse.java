package com.rares.ecommerce.auth.Keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakTokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}
