package ru.t1.java.demo.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.MetricModel;
import ru.t1.java.demo.util.ErrorType;
import ru.t1.java.demo.util.TopicName;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class MetricAspect {

    KafkaProducer kafkaProducer;

    @Around("@annotation(ru.t1.java.demo.aop.Metric)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("!!! MetricAspect.measureExecutionTime  !!!");
        Metric metric = getMetricAnnotation(joinPoint);
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            result = joinPoint.proceed();//Important

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
   /*
       Если время работы метода превышает заданное значение,
        аспект должен отправлять сообщение в топик Kafka (t1_demo_metrics) c информацией:
                о времени работы, имени метода и параметрах метода, если таковые имеются.
       В заголовке передать тип ошибки METRICS.
     */
        if (executionTime > metric.value()) {
            UUID id = UUID.randomUUID();
            MetricModel metricModel = new MetricModel(id, executionTime, joinPoint.getSignature().getName(),
                    getMethodParameters(joinPoint));

            kafkaProducer.sendTo(TopicName.T1_METRICS_TOPIC, metricModel, ErrorType.METRICS.name());
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