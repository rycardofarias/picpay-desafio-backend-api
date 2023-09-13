package com.example.picpay.exceptions;

public class DocumentValidationException extends Exception {

    public DocumentValidationException(String message) {
        super(message);
    }

    public DocumentValidationException(String message, String document) {
        super(message + document);
    }

    public DocumentValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
