package com.example.picpay.exceptions;

public class TransactionValidationException  extends Exception {

    public TransactionValidationException(String message) {
        super(message);
    }

    public TransactionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
