package com.rares.ecommerce.auth.Service;

import com.rares.ecommerce.auth.DTO.ForgotPasswordRequest;
import com.rares.ecommerce.auth.DTO.ResetPasswordRequest;
import com.rares.ecommerce.auth.Exception.AuthFlowException;
import com.rares.ecommerce.auth.Exception.TokenPurpose;
import com.rares.ecommerce.auth.Keycloak.KeycloakAdminService;
import com.rares.ecommerce.auth.Keycloak.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakAdminService keycloakAdminService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AuthProperties authProperties;

    public void forgotPassword(ForgotPasswordRequest request) {
        keycloakAdminService.findUserByEmail(request.email())
                .ifPresent(user -> {
                    String token = tokenService.issueToken(
                            user.id(),
                            user.email(),
                            TokenPurpose.PASSWORD_RESET,
                            LocalDateTime.now().plusMinutes(authProperties.resetTokenTtlMinutes())
                    );

                    emailService.sendPasswordResetEmail(user.email(), buildResetPasswordLink(token));
                });
    }

    public void resetPassword(ResetPasswordRequest request) {
        var authToken = tokenService.consumeToken(request.token(), TokenPurpose.PASSWORD_RESET);
        keycloakAdminService.resetPassword(authToken.getKeycloakUserId(), request.newPassword());
    }

    public void sendEmailVerification(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        if (keycloakUserId == null || keycloakUserId.isBlank() || email == null || email.isBlank()) {
            throw new AuthFlowException("Authenticated user does not have the required claims.");
        }

        KeycloakUserResponse user = keycloakAdminService.getUserById(keycloakUserId);
        if (Boolean.TRUE.equals(user.emailVerified())) {
            return;
        }

        String token = tokenService.issueToken(
                keycloakUserId,
                email,
                TokenPurpose.EMAIL_VERIFICATION,
                LocalDateTime.now().plusHours(authProperties.emailVerificationTtlHours())
        );

        emailService.sendEmailVerification(email, buildEmailVerificationLink(token));
    }

    public void verifyEmail(String token) {
        var authToken = tokenService.consumeToken(token, TokenPurpose.EMAIL_VERIFICATION);
        keycloakAdminService.verifyEmail(authToken.getKeycloakUserId());
    }

    private String buildResetPasswordLink(String token) {
        return authProperties.frontendBaseUrl() + "/reset-password?token=" + token;
    }

    private String buildEmailVerificationLink(String token) {
        return authProperties.frontendBaseUrl() + "/verify-email?token=" + token;
    }
}
