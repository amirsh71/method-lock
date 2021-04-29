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
    
    @Lock(timeoutSec=5, type="ORDER_FOOD")
    public void test(Request r)
    {
        try
        {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("test lock");
        System.out.println("user id " + r.userId);
        System.out.println(Thread.currentThread().getName());
    }
    
}
