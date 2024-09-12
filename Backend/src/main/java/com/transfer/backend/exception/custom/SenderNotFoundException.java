package com.transfer.backend.exception.custom;

public class SenderNotFoundException extends RuntimeException {
    public SenderNotFoundException(String message) {
        super(message);
    }
}
