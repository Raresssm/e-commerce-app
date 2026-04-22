package com.rares.ecommerce.auth.Repository;

import com.rares.ecommerce.auth.Exception.TokenPurpose;
import com.rares.ecommerce.auth.Model.AuthToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthTokenRepository extends MongoRepository<AuthToken, String> {

    Optional<AuthToken> findByTokenHashAndPurposeAndUsedAtIsNullAndExpiresAtAfter(
            String tokenHash,
            TokenPurpose purpose,
            LocalDateTime now
    );

    void deleteByKeycloakUserIdAndPurpose(String keycloakUserId, TokenPurpose purpose);
}
