package com.rares.ecommerce.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email required")
        @Email(message = "Email should be valid")
        String email
) {
}
