package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.util.ErrorType;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(0)
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {
    private final DataSourceErrorLogRepository repository;
    private final KafkaProducer kafkaProducer;

    @Value("${t1.kafka.topic.t1_demo_metrics}")
    private String metricsTopic;

    @Pointcut("within(ru.t1.java.demo.*)")
    public void loggingDataMethodsAndSaveInDB() {

    }

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)", throwing = "ex")
    @Transactional
    public void logDataExceptionAnnotation(JoinPoint joinPoint, Throwable ex) {
        String stackTrace = Arrays.toString(ex.getStackTrace());
        String message = ex.getMessage();
        String methodSignature = joinPoint.getSignature().toShortString();
        DataSourceErrorLog dataSourceErrorLog = new DataSourceErrorLog(stackTrace, message, methodSignature);
//todo сделать модель
        boolean isGone = kafkaProducer.sendForKafka(metricsTopic, dataSourceErrorLog, ErrorType.DATA_SOURCE.name());
        if (!isGone) {
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
}
