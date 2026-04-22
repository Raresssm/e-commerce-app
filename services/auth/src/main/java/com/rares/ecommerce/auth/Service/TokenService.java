package com.rares.ecommerce.auth.Service;

import com.rares.ecommerce.auth.Exception.InvalidTokenException;
import com.rares.ecommerce.auth.Exception.TokenPurpose;
import com.rares.ecommerce.auth.Model.AuthToken;
import com.rares.ecommerce.auth.Repository.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthTokenRepository repository;

    public String issueToken(String keycloakUserId, String email, TokenPurpose purpose, LocalDateTime expiresAt) {
        repository.deleteByKeycloakUserIdAndPurpose(keycloakUserId, purpose);

        String rawToken = generateToken();
        AuthToken authToken = AuthToken.builder()
                .keycloakUserId(keycloakUserId)
                .email(email)
                .tokenHash(hashToken(rawToken))
                .purpose(purpose)
                .expiresAt(expiresAt)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(authToken);
        return rawToken;
    }

    public AuthToken consumeToken(String rawToken, TokenPurpose purpose) {
        AuthToken authToken = repository.findByTokenHashAndPurposeAndUsedAtIsNullAndExpiresAtAfter(
                        hashToken(rawToken),
                        purpose,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new InvalidTokenException("Token is invalid or expired."));

        authToken.setUsedAt(LocalDateTime.now());
        repository.save(authToken);
        return authToken;
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        new java.security.SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
