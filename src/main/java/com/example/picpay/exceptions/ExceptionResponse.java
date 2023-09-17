package com.example.picpay.exceptions;

import com.example.picpay.config.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String timestamp;
    private String message;
    private String details;
    private Integer statusCode;

    public ExceptionResponse(Date timestamp, String message, String details, int statusCode) {

        this.timestamp = DateUtils.formatTimestamp(timestamp);
        this.message = message;
        this.details = details;
        this.statusCode = statusCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
