package com.transfer.backend.exception.custom;


public class EmailAlreadyUsedException extends RuntimeException  {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
