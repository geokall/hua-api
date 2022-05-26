package com.hua.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HuaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HuaNotFound.class)
    public ResponseEntity<HuaErrorResponse> handleNotFound(HuaNotFound ex, WebRequest request) {

        HuaErrorResponse response = new HuaErrorResponse();
        response.setErrorMessage(ex.getMessage() != null ? ex.getMessage() : "Not Found");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HuaUnauthorized.class)
    public ResponseEntity<HuaErrorResponse> handleUnauthorized(HuaUnauthorized ex, WebRequest request) {

        HuaErrorResponse response = new HuaErrorResponse();
        response.setErrorMessage(ex.getMessage() != null ? ex.getMessage() : "Unauthorized");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HuaForbidden.class)
    public ResponseEntity<HuaErrorResponse> handleForbidden(HuaForbidden ex, WebRequest request) {

        HuaErrorResponse response = new HuaErrorResponse();
        response.setErrorMessage(ex.getMessage() != null ? ex.getMessage() : "Forbidden");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
