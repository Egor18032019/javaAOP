package ru.t1.java.demo.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(0)
@AllArgsConstructor
public class LogDataSourceErrorAspect {
    DataSourceErrorLogRepository repository;

    @Pointcut("within(ru.t1.java.demo.*)")
    public void loggingDataMethodsAndSaveInDB() {

    }

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)")
    @Order(0)
    public void logDataExceptionAnnotation(JoinPoint joinPoint) {
        String stackTrace = Arrays.toString(Thread.currentThread().getStackTrace());
        String message = Thread.currentThread().getName();
        String methodSignature = joinPoint.getSignature().getName();
        DataSourceErrorLog dataSourceErrorLog = new DataSourceErrorLog(stackTrace, message, methodSignature);
        System.out.println(dataSourceErrorLog.toString());
        repository.save(dataSourceErrorLog);
        System.out.println("Ошибку сохранили в базу данных");
    }
}
