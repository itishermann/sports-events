package com.etna.sportevents.exceptions;

public class ForbiddenException extends Throwable {
    private final String message;

    public ForbiddenException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
