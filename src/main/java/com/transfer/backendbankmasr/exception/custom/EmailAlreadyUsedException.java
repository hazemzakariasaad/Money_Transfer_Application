package com.transfer.backendbankmasr.exception.custom;


public class EmailAlreadyUsedException extends RuntimeException  {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
