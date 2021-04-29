package com.lock.test;
import java.util.concurrent.TimeUnit;

import com.amirsh71.methodlock.core.Lock;


public class LockService
{

    
    public static class Request{
        String userId;
        String foodId;
        public Request(String userId, String foodId)
        {
            this.userId = userId;
            this.foodId = foodId;
        }
        
    }
    
    @Lock(timeoutSec=4, type="ORDER_FOOD")
    public void testTimout(Request r)
    {
        try
        {
            TimeUnit.SECONDS.sleep(6);
        }
        catch (InterruptedException e)
        {
        }
        System.out.println("test lock");
        System.out.println("user id " + r.userId);
        System.out.println(Thread.currentThread().getName());
    }
    
    @Lock(timeoutSec=4, type="ORDER")
    public void test(Request r)
    {
        System.out.println("test lock");
        System.out.println("user id " + r.userId);
        System.out.println(Thread.currentThread().getName());
    }
    
}
