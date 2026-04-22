package com.rares.ecommerce.auth.Exception;

public class AuthFlowException extends RuntimeException {

    public AuthFlowException(String message) {
        super(message);
    }

    public AuthFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
