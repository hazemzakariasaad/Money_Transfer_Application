package com.transfer.backend.exception.custom;

public class NameAlreadyUsedException extends RuntimeException{
    public NameAlreadyUsedException(String message){
    super(message);
}
}

