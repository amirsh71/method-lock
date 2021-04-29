package com.lock.test;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.amirsh71.methodlock.core.LockCacheService;
import com.amirsh71.methodlock.core.inmemory.LockCacheInMemoryServiceImpl;
import com.lock.test.LockService.Request;
import com.lock.test.LockTest.LockTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LockTestConfig.class })
public class LockTest
{

    
    @Autowired
    LockService lockService;
    
    @Test
    public void testLock() throws IOException   
    {
        Assert.notNull(lockService);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(()->{
            System.out.println("thread 1");
            sleep(6);
            lockService.test(new Request("user1", "food1"));
            lockService.test(new Request("user2", "food2"));
        });
        CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(()->{
            System.out.println("thread 2");
            sleep(6);
            lockService.test(new Request("user3", "food3"));
        });
        CompletableFuture.allOf(runAsync, runAsync2).join();
    }
    
    public void sleep(long timeout)
    {
        try
        {
            TimeUnit.SECONDS.sleep(timeout);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    @Ignore
    public void testLockTimeout() throws IOException   
    {
        Assert.notNull(lockService);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        CompletableFuture.runAsync(()->{
            try
            {
                TimeUnit.SECONDS.sleep(6);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.out.println("thread 1");
            lockService.test(new Request("user1", "food1"));
        });
        CompletableFuture.runAsync(()->{
            System.out.println("thread 2");
            lockService.test(new Request("user2", "food2"));
        });
    }

    @ComponentScan(basePackages = "com.amirsh71.methodlock.core")
    @Configuration
    @EnableAspectJAutoProxy
    public static class LockTestConfig
    {
        @Bean
        public LockCacheService lockCacheService()
        {
            return new LockCacheInMemoryServiceImpl();
//            return new ReentrantLockImpl();
        }

        @Bean
        LockService lockService()
        {
            return new LockService();
        }
    }

}