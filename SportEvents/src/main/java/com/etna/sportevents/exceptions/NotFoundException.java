package com.etna.sportevents.exceptions;

public class NotFoundException extends Throwable {
    private final String message;

    public NotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
