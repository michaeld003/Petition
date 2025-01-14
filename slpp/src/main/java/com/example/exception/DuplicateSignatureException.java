package com.example.exception;

public class DuplicateSignatureException extends RuntimeException {
    public DuplicateSignatureException(String message) {
        super(message);
    }

    public DuplicateSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
