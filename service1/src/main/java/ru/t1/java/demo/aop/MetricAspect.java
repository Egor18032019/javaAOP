package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.MetricModel;
import ru.t1.java.demo.util.ErrorType;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final KafkaProducer kafkaProducer;
    @Value("${t1.kafka.topic.t1_demo_metrics}")
    private String metricsTopic;

    @Around("@annotation(ru.t1.java.demo.aop.Metric)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Metric metric = getMetricAnnotation(joinPoint);
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            result = joinPoint.proceed();//Important

        } catch (Throwable throwable) {
            log.error("Error in method: " + joinPoint.getSignature().getName());
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (executionTime > metric.value()) {
            UUID id = UUID.randomUUID();
            MetricModel metricModel = new MetricModel(id, executionTime, joinPoint.getSignature().getName(),
                    getMethodParameters(joinPoint));

            kafkaProducer.sendForKafka(metricsTopic, metricModel, ErrorType.METRICS.name());
        }

        return result;
    }

    private Metric getMetricAnnotation(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Metric.class);
    }

    /**
     * Получение параметров метода
     *
     * @param joinPoint Метод
     * @return Параметры метода в виде строки имя параметра: значение параметра
     */
    private String getMethodParameters(ProceedingJoinPoint joinPoint) {
        StringBuilder parameters = new StringBuilder();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.append(parameterNames[i]).append(": ").append(parameterValues[i]).append(", ");
        }
        return parameters.toString();
    }
}