package com.rares.ecommerce.auth.Controller;

import com.rares.ecommerce.auth.DTO.ActionResponse;
import com.rares.ecommerce.auth.DTO.ForgotPasswordRequest;
import com.rares.ecommerce.auth.DTO.ResetPasswordRequest;
import com.rares.ecommerce.auth.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ActionResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ActionResponse("If the account exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ActionResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new ActionResponse("Password updated successfully."));
    }

    @PostMapping("/send-email-verification")
    public ResponseEntity<ActionResponse> sendEmailVerification(@AuthenticationPrincipal Jwt jwt) {
        authService.sendEmailVerification(jwt);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ActionResponse("Verification email scheduled."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ActionResponse> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(new ActionResponse("Email verified successfully."));
    }
}
