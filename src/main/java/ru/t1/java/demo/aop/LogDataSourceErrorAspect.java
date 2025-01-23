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
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.util.ErrorType;
import ru.t1.java.demo.util.TopicName;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(0)
@AllArgsConstructor
public class LogDataSourceErrorAspect {
    DataSourceErrorLogRepository repository;
    KafkaProducer kafkaProducer;

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
        kafkaProducer.sendTo(TopicName.T1_METRICS_TOPIC, dataSourceErrorLog, ErrorType.DATA_SOURCE.name());
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
/*
    В первую очередь аспект должен отсылать сообщение в топик t1_demo_metrics.
    В заголовке должен указываться тип ошибки: DATA_SOURCE;
    В случае, если отправка не удалась - записать в БД.
 */