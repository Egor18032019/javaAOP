package ru.t1.java.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionAccept;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.AccountStatus;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAccountConsumer {

    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final KafkaProducer kafkaProducer;

    private final TransactionService transactionService;
    @Value("${t1.kafka.topic.t1_demo_accounts}")
    private String accountsTopic;

    @Value("${t1.kafka.topic.t1_demo_transactions}")
    private String transactionsTopic;
    @Value("${t1.kafka.topic.t1_demo_transaction_accept}")
    private String transactionsTopicAccept;
    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = {"${t1.kafka.topic.t1_demo_accounts}",
                    "${t1.kafka.topic.t1_demo_transactions}"
            },
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload String message,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
//                         @Header(KafkaHeaders.NATIVE_HEADERS) Map<String, Object> nativeHeaders
    ) throws IOException {
        log.debug("Client consumer2: Обработка новых сообщений");
/*
- Если транзакции по одному и тому же клиенту и счету приходят больше N раз в Т времени (настраивается в конфиге)
и timestamp транзакции попадает в этот период,
то N транзакциям присвоить статус BLOCKED,
сообщение со статусом, id счета и id транзакции отправить в топик t1_demo_transaction_result


- Если сумма списания в транзакции больше, чем баланс счета
    -> отправить сообщение со статусом REJECTED

- Если всё ок, то статус ACCECPTED
 */
        try {
            log.info("Получено сообщение из топика {}: {}", topic, message);


        } finally {
            ack.acknowledge();
        }
        log.debug("KafkaTransactionAccountConsumer consumer: записи обработаны");
    }
}
