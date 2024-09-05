package com.transfer.backendbankmasr.exception.custom;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}