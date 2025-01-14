package com.example.exception;

public class InvalidBioIdException extends RuntimeException {
    public InvalidBioIdException(String message) {
        super(message);
    }
}

