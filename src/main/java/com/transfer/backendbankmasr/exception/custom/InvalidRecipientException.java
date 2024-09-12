package com.transfer.backendbankmasr.exception.custom;

public class InvalidRecipientException extends RuntimeException {
    public InvalidRecipientException(String message) {
        super(message);
    }
}
