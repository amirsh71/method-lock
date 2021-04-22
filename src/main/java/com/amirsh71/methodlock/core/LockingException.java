package com.amirsh71.methodlock.core;

/**
 * an <code>LockingException</code> thrown if putting lock key to cache fails.
 * @author a.shokri (shokri4971@gmail.com)
 * @since 2017-08-11
 */
public class LockingException extends RuntimeException {
    private final Exception exception;

    public LockingException(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
