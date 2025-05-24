package com.ticketnow.crud.exception;

public class CustomCrudException extends RuntimeException{
    private final String code;

    public CustomCrudException(ErrorMessage message) {
        super(message.getMessage());
        this.code = message.getCode();
    }

    public String getCode() {
        return code;
    }
}
