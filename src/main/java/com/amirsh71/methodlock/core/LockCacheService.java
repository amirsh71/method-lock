package com.amirsh71.methodlock.core;

import java.util.Optional;

/**
 * @author a.shokri (shokri4971@gmail.com)
 * @since 2017-08-11
 */
public abstract class LockCacheService {
    public abstract String lock(LockObject lock, Optional<Integer> lockTimeout);

    public abstract void unLock(String lockKey);
}