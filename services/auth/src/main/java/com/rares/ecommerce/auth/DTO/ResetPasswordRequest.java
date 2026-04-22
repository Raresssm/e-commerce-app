package com.rares.ecommerce.auth.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Token required")
        String token,
        @NotBlank(message = "New password required")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String newPassword
) {
}
