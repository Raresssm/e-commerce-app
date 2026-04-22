package com.rares.ecommerce.auth.DTO;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
