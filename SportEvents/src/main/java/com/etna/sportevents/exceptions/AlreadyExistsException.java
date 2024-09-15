package com.etna.sportevents.exceptions;

public class AlreadyExistsException extends Throwable {
    private final String message;

    public AlreadyExistsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
