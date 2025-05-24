package com.ticketnow.crud.exception.handler;


import com.ticketnow.crud.exception.CustomCrudException;
import com.ticketnow.crud.exception.ErrorMessage;
import com.ticketnow.crud.exception.NotFoundException;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
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

    @ExceptionHandler(CustomCrudException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<ErrorMessage> customLogicExceptionHandler(CustomCrudException ex) {
        return new APIResponse<>(null, ex.getMessage(), ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleException(Exception e) {
        logger.error("Exception in {}.{}() with cause = {}", e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(), e.getCause() != null ? e.getCause() : e.getStackTrace());
        return new APIResponseBuilder<>().exception(ErrorMessage.UNKNOWN_ERROR).build();
    }

}
