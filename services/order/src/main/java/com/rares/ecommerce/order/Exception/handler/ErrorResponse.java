package com.rares.ecommerce.order.Exception.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
