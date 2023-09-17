package com.example.picpay.exceptions;

import org.springframework.http.HttpStatus;

public class NotificationException extends Exception{

    private final HttpStatus statusCode;

    public NotificationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
