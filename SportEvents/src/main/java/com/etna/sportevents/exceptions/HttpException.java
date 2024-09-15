package com.etna.sportevents.exceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends Throwable {
    private final String message;
    private final HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static HttpException notFound(String message) {
        return new HttpException(message, HttpStatus.NOT_FOUND);
    }

    public static HttpException forbidden(String message) {
        return new HttpException(message, HttpStatus.FORBIDDEN);
    }

    public static HttpException conflict(String message) {
        return new HttpException(message, HttpStatus.CONFLICT);
    }
}

