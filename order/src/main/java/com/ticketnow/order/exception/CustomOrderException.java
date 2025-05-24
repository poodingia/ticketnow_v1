package com.ticketnow.order.exception;

public class CustomOrderException extends RuntimeException {
    private final String code;

    public CustomOrderException(ErrorMessage message) {
        super(message.getMessage());
        this.code = message.getCode();
    }

    public String getCode() {
        return code;
    }
}

