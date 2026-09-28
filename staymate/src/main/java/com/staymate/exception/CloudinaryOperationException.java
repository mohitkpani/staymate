package com.staymate.exception;

public class CloudinaryOperationException extends RuntimeException {

    public CloudinaryOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}