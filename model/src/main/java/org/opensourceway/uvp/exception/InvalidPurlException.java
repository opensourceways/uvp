package org.opensourceway.uvp.exception;

public class InvalidPurlException extends RuntimeException {
    public InvalidPurlException() {
        super();
    }

    public InvalidPurlException(String message) {
        super(message);
    }

    public InvalidPurlException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPurlException(Throwable cause) {
        super(cause);
    }

    protected InvalidPurlException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
