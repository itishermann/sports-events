package com.etna.sportevents.exceptions;

public class InvalidArgException extends Exception {
    private final String message;

    public InvalidArgException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
