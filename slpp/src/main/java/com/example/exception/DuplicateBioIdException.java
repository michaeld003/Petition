package com.example.exception;


public class DuplicateBioIdException extends RuntimeException {
    public DuplicateBioIdException(String message) {
        super(message);
    }
    public DuplicateBioIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

