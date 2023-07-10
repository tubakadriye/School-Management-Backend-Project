package com.project.schoolmanagment.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution(* com.project.schoolmanagment.service.user.AdminService.*(..))")
    public void createLogForAdminServiceMethods(JoinPoint joinPoint) {
        logAuthentication(joinPoint);
    }

    @After("execution(* com.project.schoolmanagment.service.*.*.delete*(..))")
    public void createLogForDeleteOperations(JoinPoint joinPoint) {
        logAuthentication(joinPoint);
    }

    @AfterThrowing(pointcut = "execution(* com.project.schoolmanagment.*.*.*.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in method {}: {}", joinPoint.getSignature().getName(), e.getMessage());
    }

    @Around("execution(* com.project.schoolmanagment.service.*.*.*(..))")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long duration = end - start;
        if(duration>4500) {
            log.info("Method {} in class {} took {} ms to execute", joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getSimpleName(), duration);
        }
        return result;
    }


    private void logAuthentication(JoinPoint joinPoint){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            log.info("Username : " + userDetails.getUsername() + " , " + "Method Name : " + joinPoint.getSignature().getName());
        }
    }

}
