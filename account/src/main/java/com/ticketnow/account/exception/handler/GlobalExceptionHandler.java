package com.ticketnow.account.exception.handler;


import com.ticketnow.account.exception.ErrorMessage;
import com.ticketnow.account.exception.NotFoundException;
import com.ticketnow.account.rest.response.APIResponse;
import com.ticketnow.account.rest.response.APIResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleException(Exception e) {
        logger.error("Exception in {}.{}() with cause = {}", e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(), e.getCause() != null ? e.getCause() : e.getStackTrace(), e);
        return new APIResponseBuilder<>().exception(ErrorMessage.UNKNOWN_ERROR).build();
    }

}
