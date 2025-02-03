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
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.AccountStatus;
import ru.t1.java.demo.util.TransactionStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAccountConsumer {

    private final ObjectMapper objectMapper;

    private final KafkaProducer kafkaProducer;
    private final TransactionRepository transactionRepository;

    @Value("${t1.kafka.topic.t1_demo_transaction_result}")
    private String transactionsTopicResult;

    @Value("${t1.kafka.transaction.timeout}")
    private Long transactionTimeout;
    @Value("${t1.kafka.transaction.max-transactions}")
    private int maxTransaction;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = {"${t1.kafka.topic.t1_demo_transaction_accept}"},
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

- Если сумма списания в транзакции больше, чем баланс счета
    -> отправить сообщение со статусом REJECTED

- Если всё ок, то статус ACCECPTED

- Сообщение со статусом, id счета и id транзакции отправить в топик t1_demo_transaction_result
 */
        try {
            log.info("Получено сообщение из топика {}: {}", topic, message);
            TransactionAccept transactionFromKafka = objectMapper.readValue(message, TransactionAccept.class);
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusSeconds(transactionTimeout);
            int transactionCount = transactionRepository.countByAccountIdAndTimestampBetween(transactionFromKafka.getAccountId(),
                    startTime,
                    endTime);
//            endTime.minusNanos(1)); // Исключаем endTime
            System.out.println(transactionCount);

            Transaction transactionComming = transactionRepository.findByTransactionId(transactionFromKafka.getTransactionId());

            if (transactionCount > maxTransaction) {
                transactionComming.setTransactionStatus(TransactionStatus.BLOCKED);
            } else {
                if (transactionFromKafka.getTransactionAmount() > transactionFromKafka.getAccountBalance()) {
                    transactionComming.setTransactionStatus(TransactionStatus.REJECTED);

                } else {
                    transactionComming.setTransactionStatus(TransactionStatus.ACCECPTED);

                }
            }

            kafkaProducer.send(new TransactionResultDto(transactionComming.getTransactionId(), transactionComming.getAccountId(), transactionComming.getTransactionStatus()),
                    transactionsTopicResult);
        } finally {
            ack.acknowledge();
        }
        log.debug("KafkaTransactionAccountConsumer consumer: записи обработаны");
    }
}
