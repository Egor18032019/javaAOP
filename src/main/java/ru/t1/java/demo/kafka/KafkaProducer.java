package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> template;

    public void sendTo(String topic, Object o, String error) {
        UUID uuid = UUID.randomUUID();
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, uuid.toString(), o);
        record.headers().add("error-code", error.getBytes());
        try {
            template.send(record).get();
            log.info("отправили в кафку - " + topic);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public boolean sendForKafka(String topic, Object o, String error) {

        UUID uuid = UUID.randomUUID();
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, uuid.toString(), o);
        record.headers().add("error-code", error.getBytes());

        CompletableFuture<SendResult<String, Object>> future = template.send(record);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Сообщение успешно отправлено в Kafka. Топик: {}, Партиция: {}, Оффсет: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Ошибка при отправке сообщения в Kafka. Топик: {}", topic, exception);
            }
        });

        return future.isDone();
    }

    public void send(Object clientDto, String topic) {
        UUID uuid = UUID.randomUUID();
        log.info("отправили в кафку - " + topic);
        template.send(topic, uuid.toString(), clientDto);
    }
}
