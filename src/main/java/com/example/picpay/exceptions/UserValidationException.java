package com.example.picpay.exceptions;

public class UserValidationException extends Exception {

    public UserValidationException(String message) {
        super(message);
    }

    public UserValidationException(String message, Long id) {
        super(message + id);
    }

    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
