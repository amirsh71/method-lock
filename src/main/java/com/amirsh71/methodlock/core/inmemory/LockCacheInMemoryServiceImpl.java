package com.amirsh71.methodlock.core.inmemory;

import com.amirsh71.methodlock.core.LockCacheService;
import com.amirsh71.methodlock.core.LockObject;
import com.amirsh71.methodlock.core.OperationLockedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LockCacheInMemoryServiceImpl extends LockCacheService {
    private static final Logger LOGGER = LogManager.getLogger(LockCacheInMemoryServiceImpl.class);
    private final Map<String, LocalDateTime> CACHE = new ConcurrentHashMap<>();

    @Override
    public String lock(LockObject lock, Optional<Integer> lockTimeout) {
        if (null != CACHE.putIfAbsent(lock.getLockKey(), LocalDateTime.now())) {
            throw new OperationLockedException(lock);
        }
        LOGGER.debug("key {} locked", lock.getLockKey());
        return lock.getLockKey();
    }

    @Override
    public void unLock(String lockKey) {
        try {
            CACHE.remove(lockKey);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + e.toString());
        }
    }
}
