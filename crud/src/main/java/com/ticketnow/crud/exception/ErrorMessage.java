package com.ticketnow.crud.exception;

public enum ErrorMessage {
    SUCCESS("00000", "Success"),
    INVALID_INPUT_DATA("LG001", "Invalid input data"),
    UNKNOWN_ERROR("LG002", "Unknown error"),
    INVALID_COUPON("LG003", "Invalid coupon"),;

    private final String code;
    private final String message;

    ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
