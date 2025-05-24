package com.ticketnow.order.exception;

public enum ErrorMessage {
    SUCCESS("00000", "Success"),
    INVALID_INPUT_DATA("LG001", "Invalid input data"),
    UNKNOWN_ERROR("LG002", "Unknown error"),
    SESSION_INVALID("LG003", "Session invalid"),
    UNAUTHORIZED("LG005", "You are not authorized to view this"),
    TICKET_TYPE_INVALID("LG004", "Ticket type invalid"),;

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
