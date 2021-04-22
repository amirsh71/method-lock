package com.amirsh71.methodlock.core;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author a.shokri (shokri4971@gmail.com)
 * @since 2017-08-11
 */
public class LockObject {
    private final String serviceName;
    private final String type;
    private final Object[] lockParams;

    public LockObject(String serviceName, String type, Object[] lockParams) {
        this.serviceName = serviceName;
        this.type = type;
        this.lockParams = lockParams;
    }

    public String getLockKey() {
        return "LOCK::" + serviceName + "::" + type + "::" + Arrays.stream(lockParams).map(Object::toString).collect(Collectors.joining("::"));
    }

    @Override
    public String toString() {
        return "LockObject{" +
                "serviceName='" + serviceName + '\'' +
                ", type='" + type + '\'' +
                ", lockParams=" + Arrays.toString(lockParams) +
                '}';
    }
}
