package com.wipro.apigateway.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracingAspect {
    private static final Logger logger = LoggerFactory.getLogger(TracingAspect.class);

    @Around("execution(* com.wipro.apigateway..*(..))")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Entering method: {}", methodName);
        try {
            Object result = joinPoint.proceed();
            logger.info("Exiting method: {}", methodName);
            return result;
        } catch (Throwable t) {
            logger.error("Exception in method {}: {}", methodName, t.getMessage());
            throw t;
        }
    }
}