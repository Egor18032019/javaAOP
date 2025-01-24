package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClientConsumerAccounts {
    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = {"t1_demo_accounts"},
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<ClientDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Client consumer: Обработка новых сообщений");


        try {
            log.error("Topic: " + topic);
            log.error("Key: " + key);
            messageList.stream()
                    .forEach(System.err::println);

        } finally {
            ack.acknowledge();
        }


        log.debug("Client consumer: записи обработаны");
    }
}
/*
3. Реализовать 2 консьюмера, слушающих топики t1_demo_accounts и t1_demo_transactions.
   При получении сообщения сервис должен сохранять счет и транзакцию в бд.
    (Консьюмер и код, сохраняющий клиента, есть в проекте)
   В качестве ключей к сообщениям можно генерировать случайный UUID.
 */