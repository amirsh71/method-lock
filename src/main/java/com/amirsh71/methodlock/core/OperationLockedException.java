package com.amirsh71.methodlock.core;

/**
 * an <code>OperationLockedException</code> thrown if acquiring lock object fails.
 *
 * @author a.shokri (shokri4971@gmail.com)
 * @since 2017-08-11
 */
public class OperationLockedException extends RuntimeException {
    private final LockObject lockObject;

    public OperationLockedException(LockObject lockObject) {
        super(String.format("operation is locked. %s", lockObject.toString()));
        this.lockObject = lockObject;
    }

    public LockObject getLockObject() {
        return lockObject;
    }
}
