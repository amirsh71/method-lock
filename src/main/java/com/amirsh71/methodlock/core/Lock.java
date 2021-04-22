package com.amirsh71.methodlock.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation is generate a wrapper (AOP) around a method, that locks that method based on given parameters and
 * prevent other thread (by equal parameters) enter that method until previous thread is in method.
 *
 * @author a.shokri (shokri4971@gmail.com)
 * @see LockingException
 * @see OperationLockedException
 * @since 2017-08-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    /**
     * a string key that for type of lock, for example: "ORDER_FOOD"
     */
    String type();

    /**
     * not required! an array of parameters for using in lock object, for example billId.
     * you can use Spring Expression Language (SpEL) to use values of methods parameters
     *
     * <p>params = {"#request.customerId","#request.foodId"}</p>
     * <p>params = {"#c.list.![b.id]","#param", "#param2"}</p>
     */
    String[] params() default {};

    /**
     * timeout of lock. if not set default value of overallTimeout will be used.
     */
    int timeoutSec() default 0;
}
