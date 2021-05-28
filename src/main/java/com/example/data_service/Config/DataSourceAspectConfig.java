package com.example.data_service.Config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DataSourceAspectConfig {
    @Pointcut("@annotation(com.example.data_service.Annotation.Master)")
    public void WritePointCut(){}

    @Pointcut("@annotation(com.example.data_service.Annotation.Slave)")
    public void ReadPointCut(){}

    @Before("WritePointCut()")
    public void write(){
        System.out.println("尝试切换至主库");
        DBContext.set_master();
        System.out.println("切换主库成功");
    }

    @Before("ReadPointCut()")
    public void read(){
        System.out.println("尝试切换至从库");
        DBContext.set_slave();
        System.out.println("切换从库成功");
    }

}
