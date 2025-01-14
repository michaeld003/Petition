package com.example.exception;

public class PetitionNotFoundException extends RuntimeException {
    public PetitionNotFoundException(String message) {
        super(message);
    }

    public PetitionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
