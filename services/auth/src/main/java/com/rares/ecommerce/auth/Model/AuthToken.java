package com.rares.ecommerce.auth.Model;

import com.rares.ecommerce.auth.Exception.TokenPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "auth_tokens")
public class AuthToken {

    @Id
    private String id;
    private String keycloakUserId;
    private String email;
    private String tokenHash;
    private TokenPurpose purpose;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
}
