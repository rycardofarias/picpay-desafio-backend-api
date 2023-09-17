package com.example.picpay.exceptions.handler;

import com.example.picpay.exceptions.DocumentValidationException;
import com.example.picpay.exceptions.ExceptionResponse;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionValidationException.class)
    public final ResponseEntity<ExceptionResponse> handleTransactionValidationException(TransactionValidationException ex,  WebRequest request) {

        return createExceptionResponse(ex.getMessage(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(UserValidationException.class)
    public final ResponseEntity<ExceptionResponse> handleUserValidationException(UserValidationException ex,  WebRequest request) {

        return createExceptionResponse(ex.getMessage(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(DocumentValidationException.class)
    public final ResponseEntity<ExceptionResponse> handleUserValidationException(DocumentValidationException ex,  WebRequest request) {

        return createExceptionResponse(ex.getStatusCode(), request);
    }

    public final ResponseEntity<ExceptionResponse> handleAllExceptins(Exception ex, WebRequest request){

        return createExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ExceptionResponse> createExceptionResponse(String message, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                message,
                request.getDescription(false),
                status.value()
        );

        return new ResponseEntity<>(exceptionResponse, status);
    }

    private ResponseEntity<ExceptionResponse> createExceptionResponse(HttpStatus status, WebRequest request) {
        return createExceptionResponse(status.getReasonPhrase(), status, request);
    }
}
