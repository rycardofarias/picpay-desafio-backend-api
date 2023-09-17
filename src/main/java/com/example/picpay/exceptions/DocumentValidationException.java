package com.example.picpay.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentValidationException extends Exception {

    private final HttpStatus statusCode;

    public DocumentValidationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public DocumentValidationException(String message, String document, HttpStatus statusCode) {
        super(message + document);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
