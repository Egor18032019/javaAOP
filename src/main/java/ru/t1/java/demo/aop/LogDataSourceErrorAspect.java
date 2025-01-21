package ru.t1.java.demo.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    @Order(0)
    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)", throwing = "ex")
    @Transactional
    public void logDataExceptionAnnotation(JoinPoint joinPoint, Throwable ex) {
        String stackTrace = Arrays.toString(ex.getStackTrace());
        String message = ex.getMessage();
        String methodSignature = joinPoint.getSignature().toShortString();
        DataSourceErrorLog dataSourceErrorLog = new DataSourceErrorLog(stackTrace, message, methodSignature);
        try {
            log.info("Начали сохранять в БД.");
            repository.save(dataSourceErrorLog);
            log.info("Ошибку сохранили в базу данных");
        } catch (Exception e) {
            log.error("Ошибка сохранения в базу данных");
        } finally {
            log.error("Закончили сохранять в БД.");
        }
    }
}
