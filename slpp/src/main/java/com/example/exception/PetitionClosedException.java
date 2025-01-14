package com.example.exception;

public class PetitionClosedException extends RuntimeException {
    public PetitionClosedException(String message) {
        super(message);
    }

    public PetitionClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}