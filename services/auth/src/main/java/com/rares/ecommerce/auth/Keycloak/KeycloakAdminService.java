package com.rares.ecommerce.auth.Keycloak;

import com.rares.ecommerce.auth.Exception.AuthFlowException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final RestClient.Builder restClientBuilder;
    private final KeycloakAdminProperties properties;

    public Optional<KeycloakUserResponse> findUserByEmail(String email) {
        try {
            List<KeycloakUserResponse> users = restClientBuilder.build()
                    .get()
                    .uri(buildUsersUri(email))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + requestAdminAccessToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (users == null) {
                return Optional.empty();
            }

            return users.stream()
                    .filter(user -> user.email() != null && user.email().equalsIgnoreCase(email))
                    .findFirst();
        } catch (RestClientResponseException ex) {
            throw new AuthFlowException("Failed to search user in Keycloak: " + ex.getResponseBodyAsString(), ex);
        }
    }

    public KeycloakUserResponse getUserById(String userId) {
        try {
            return restClientBuilder.build()
                    .get()
                    .uri(buildUserByIdUri(userId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + requestAdminAccessToken())
                    .retrieve()
                    .body(KeycloakUserResponse.class);
        } catch (RestClientResponseException ex) {
            throw new AuthFlowException("Failed to fetch Keycloak user: " + ex.getResponseBodyAsString(), ex);
        }
    }

    public void resetPassword(String userId, String newPassword) {
        Map<String, Object> payload = Map.of(
                "type", "password",
                "value", newPassword,
                "temporary", false
        );

        try {
            restClientBuilder.build()
                    .put()
                    .uri(buildResetPasswordUri(userId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + requestAdminAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            throw new AuthFlowException("Failed to reset Keycloak password: " + ex.getResponseBodyAsString(), ex);
        }
    }

    public void verifyEmail(String userId) {
        KeycloakUserResponse user = getUserById(userId);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.id());
        payload.put("username", user.username());
        payload.put("firstName", user.firstName());
        payload.put("lastName", user.lastName());
        payload.put("email", user.email());
        payload.put("enabled", user.enabled() == null || user.enabled());
        payload.put("emailVerified", true);

        try {
            restClientBuilder.build()
                    .put()
                    .uri(buildUserByIdUri(userId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + requestAdminAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            throw new AuthFlowException("Failed to verify Keycloak email: " + ex.getResponseBodyAsString(), ex);
        }
    }

    private String requestAdminAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", properties.adminClientId());
        formData.add("username", properties.adminUsername());
        formData.add("password", properties.adminPassword());

        try {
            KeycloakTokenResponse response = restClientBuilder.build()
                    .post()
                    .uri(buildTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(KeycloakTokenResponse.class);

            if (response == null || response.accessToken() == null || response.accessToken().isBlank()) {
                throw new AuthFlowException("Keycloak admin token response was empty");
            }

            return response.accessToken();
        } catch (RestClientResponseException ex) {
            throw new AuthFlowException("Failed to obtain Keycloak admin token: " + ex.getResponseBodyAsString(), ex);
        }
    }

    private URI buildTokenUri() {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("realms", properties.adminRealm(), "protocol", "openid-connect", "token")
                .build()
                .toUri();
    }

    private URI buildUsersUri(String email) {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("admin", "realms", properties.realm(), "users")
                .queryParam("email", email)
                .queryParam("exact", true)
                .build()
                .toUri();
    }

    private URI buildUserByIdUri(String userId) {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("admin", "realms", properties.realm(), "users", userId)
                .build()
                .toUri();
    }

    private URI buildResetPasswordUri(String userId) {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("admin", "realms", properties.realm(), "users", userId, "reset-password")
                .build()
                .toUri();
    }
}
