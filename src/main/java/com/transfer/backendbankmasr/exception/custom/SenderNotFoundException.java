package com.transfer.backendbankmasr.exception.custom;

public class SenderNotFoundException extends RuntimeException {
    public SenderNotFoundException(String message) {
        super(message);
    }
}
