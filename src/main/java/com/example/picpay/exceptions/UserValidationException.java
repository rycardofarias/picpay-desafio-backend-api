package com.example.picpay.exceptions;

import org.springframework.http.HttpStatus;

public class UserValidationException extends Exception {

    private final HttpStatus statusCode;

    public UserValidationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public UserValidationException(String message, Long id, HttpStatus statusCode) {
        super(message + id);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
