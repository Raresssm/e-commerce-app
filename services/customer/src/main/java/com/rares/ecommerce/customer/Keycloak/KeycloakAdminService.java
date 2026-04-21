package com.rares.ecommerce.customer.Keycloak;

import com.rares.ecommerce.customer.DTO.RegisterRequest;
import com.rares.ecommerce.customer.Exceptions.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final RestClient.Builder restClientBuilder;
    private final KeycloakAdminProperties properties;

    public String createUser(RegisterRequest request) {
        String accessToken = requestAdminAccessToken();
        URI usersUri = buildUsersUri();

        Map<String, Object> payload = Map.of(
                "username", request.email(),
                "email", request.email(),
                "firstName", request.firstname(),
                "lastName", request.lastname(),
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", request.password(),
                        "temporary", false
                ))
        );

        try {
            var response = restClientBuilder.build()
                    .post()
                    .uri(usersUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            URI location = response.getHeaders().getLocation();
            if (location == null) {
                throw new RegistrationException("Keycloak user was created without a location header");
            }

            String path = location.getPath();
            int separatorIndex = path.lastIndexOf('/');
            return path.substring(separatorIndex + 1);
        } catch (RestClientResponseException ex) {
            throw new RegistrationException("Keycloak user registration failed: " + ex.getResponseBodyAsString(), ex);
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
                throw new RegistrationException("Keycloak admin token response was empty");
            }

            return response.accessToken();
        } catch (RestClientResponseException ex) {
            throw new RegistrationException("Failed to obtain Keycloak admin token: " + ex.getResponseBodyAsString(), ex);
        }
    }

    private URI buildTokenUri() {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("realms", properties.adminRealm(), "protocol", "openid-connect", "token")
                .build()
                .toUri();
    }

    private URI buildUsersUri() {
        return UriComponentsBuilder.fromHttpUrl(properties.serverUrl())
                .pathSegment("admin", "realms", properties.realm(), "users")
                .build()
                .toUri();
    }
}
