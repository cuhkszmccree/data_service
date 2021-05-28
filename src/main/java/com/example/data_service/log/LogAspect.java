package com.example.data_service.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
@Slf4j
public class LogAspect {
    @Before("execution(* com.example.data_service.Service.Impl.PDataImpl.*(..))")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("Mysql " + method.getName() + " operation Prepared");
        //System.out.println("Log Aspect " + method.getName());
    }
    @After("execution(* com.example.data_service.Service.Impl.PDataImpl.*(..))")
    public void after(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("Mysql " + method.getName() + " operation Success");
        //System.out.println("Log Aspect " + method.getName());
    }
}