package com.transfer.backendbankmasr.exception.custom;

public class NameAlreadyUsedException extends RuntimeException{
    public NameAlreadyUsedException(String message){
    super(message);
}
}

