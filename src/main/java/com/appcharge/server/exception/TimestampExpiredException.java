package com.appcharge.server.exception;

public class TimestampExpiredException extends RuntimeException {

    public TimestampExpiredException(String message) {
        super(message);
    }

    public TimestampExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
