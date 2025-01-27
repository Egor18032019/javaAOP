package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.ClientDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> template;


    public void sendTo(String topic, Object o, String error) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic,"key", o);
        record.headers().add("error-code", error.getBytes());
        try {
            template.send(record).get();
            System.out.println("отправили в кафку - ");

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void send(ClientDto clientDto, String topic) {
        template.send(topic, clientDto);
    }
}
