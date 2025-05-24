package com.ticketnow.crud.rest.response;

import java.util.Date;

public class APIResponse<T> {
    private T data;
    private String message;
    private String code;
    private final long timeStamp;

    public APIResponse(T data, String code) {
        this.data = data;
        this.code = code;
        this.timeStamp = new Date().getTime() / 10000L;
    }

    public APIResponse(T data, String message, String code) {
        this.data = data;
        this.message = message;
        this.code = code;
        this.timeStamp = new Date().getTime() / 10000L;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
