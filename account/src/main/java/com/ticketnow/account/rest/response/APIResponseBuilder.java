package com.ticketnow.account.rest.response;

import com.ticketnow.account.exception.ErrorMessage;

public class APIResponseBuilder<T> {
    private T data;
    private String message;
    private String code;

    public APIResponseBuilder() {

    }

    public APIResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public APIResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public APIResponseBuilder<T> code(String code) {
        this.code = code;
        return this;
    }

    public APIResponseBuilder<T> ok() {
        this.code = "00000";
        this.message = "";
        return this;
    }

    public APIResponseBuilder<T> exception(ErrorMessage errorMessage) {
        this.code = errorMessage.getCode();
        this.message = errorMessage.getMessage();
        this.data = null;
        return this;
    }

    public APIResponse<T> build() {
        return new APIResponse<>(data, message, code);
    }
}
