package com.example.insurance.mis.mvp.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.insurance.mis.mvp.controllers..*(..))")
    public Object aroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before executing controller method: " + joinPoint.getSignature());
        Object result = joinPoint.proceed();
        log.info("After executing controller method: " + joinPoint.getSignature());
        return result;
    }

    @Around("execution(* com.example.insurance.mis.mvp.services..*(..))")
    public Object aroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before executing service method: " + joinPoint.getSignature());
        Object result = joinPoint.proceed();
        log.info("After executing service method: " + joinPoint.getSignature());
        return result;
    }
}
