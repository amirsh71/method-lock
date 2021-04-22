package com.amirsh71.methodlock.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author a.shokri (shokri4971@gmail.com)
 * @since 2017-08-11
 */
@Aspect
@Component
@Primary
public class LockAspect {
    private static final Logger LOGGER = LogManager.getLogger(LockAspect.class);
    private final LockCacheService lockCacheService;
    @Value("${spring.application.name:SERVICE}")
    private String serviceName;

    ExpressionParser parser = new SpelExpressionParser();
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public LockAspect(LockCacheService lockCacheService) {
        this.lockCacheService = lockCacheService;
    }

    @Pointcut("@annotation(com.amirsh71.methodlock.core.Lock)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Lock annotation = method.getAnnotation(Lock.class);

        List<Object> lockParams = this.getLockParams(annotation, method, pjp.getArgs());
        Optional<Integer> lockTimeout = this.getLockTimeout(annotation);

        String lock = lockCacheService.lock(new LockObject(serviceName, annotation.type(), lockParams.toArray()), lockTimeout);
        try {
            return pjp.proceed();
        } finally {
            lockCacheService.unLock(lock);
        }
    }

    private Optional<Integer> getLockTimeout(Lock annotation) {
        int timeout = annotation.timeoutSec();
        return timeout == 0 ? Optional.empty() : Optional.of(timeout);
    }

    private List<Object> getLockParams(Lock annotation, Method method, Object[] args) {
        List<Object> list = new ArrayList<>();
        String[] lockParams = annotation.params();
        for (String lockParam : lockParams) {
            String[] paramList = discoverer.getParameterNames(method);
            EvaluationContext context = new StandardEvaluationContext();
            for (int len = 0; len < paramList.length; len++) {
                context.setVariable(paramList[len], args[len]);
            }
            Expression expression = parser.parseExpression(lockParam);
            Object value = expression.getValue(context, Object.class);
            if (value == null) {
                LOGGER.warn("value of given expression is null!!, expression={}", lockParam);
                value = "EMPTY";
            }
            list.add(value);
        }
        return list;
    }
}