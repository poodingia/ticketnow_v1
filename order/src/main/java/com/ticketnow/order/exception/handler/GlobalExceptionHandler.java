package com.ticketnow.order.exception.handler;

import com.ticketnow.order.exception.CustomOrderException;
import com.ticketnow.order.exception.ErrorMessage;
import com.ticketnow.order.exception.NotFoundException;
import com.ticketnow.order.rest.response.APIResponse;
import com.ticketnow.order.rest.response.APIResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<?> handleBindException(BindException e) {
        return new APIResponseBuilder<>().exception(ErrorMessage.INVALID_INPUT_DATA).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<?> handleNotFoundException(NotFoundException e) {
        return new APIResponseBuilder<>().exception(ErrorMessage.INVALID_INPUT_DATA).build();
    }

    @ExceptionHandler(CustomOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<ErrorMessage> customLogicExceptionHandler(CustomOrderException ex) {
        return new APIResponse<>(null, ex.getMessage(), ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleException(Exception e) {
        return new APIResponseBuilder<>().exception(ErrorMessage.UNKNOWN_ERROR).build();
    }
}
