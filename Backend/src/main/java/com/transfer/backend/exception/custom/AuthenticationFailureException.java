package com.transfer.backend.exception.custom;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}