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
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockImpl extends LockCacheService {
    private static final Logger LOGGER = LogManager.getLogger(ReentrantLockImpl.class);

    ReentrantLock locker = new ReentrantLock();
    
    @Override
    public String lock(LockObject lock, Optional<Integer> lockTimeout) {
        locker.lock();        
        return lock.getLockKey();
    }

    @Override
    public void unLock(String lockKey) {
        try {
            locker.unlock();
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + e.toString());
        }
    }
}
