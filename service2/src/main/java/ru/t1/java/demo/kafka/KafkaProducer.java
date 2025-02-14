package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> template;


    public void send(Object clientDto, String topic) {
        UUID uuid = UUID.randomUUID();
        log.info("отправили в кафку - " + topic);
        template.send(topic, uuid.toString(), clientDto);
    }
}
