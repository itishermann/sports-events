package com.etna.sportevents.exceptions;

public class IncorrectCredentialException extends Throwable {
    private final String message;

    public IncorrectCredentialException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
