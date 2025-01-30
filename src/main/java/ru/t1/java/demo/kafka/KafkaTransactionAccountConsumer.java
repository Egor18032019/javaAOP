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
import ru.t1.java.demo.util.TransactionStatus;

import java.io.IOException;
import java.time.LocalDateTime;

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
        log.debug("Client consumer: Обработка новых сообщений");

        try {
            log.info("Получено сообщение из топика {}: {}", topic, message);
            if (topic.equals(accountsTopic)) {
                AccountDto accountDto = objectMapper.readValue(message, AccountDto.class);
                accountService.saveAccountDto(accountDto);
            }
            else if (topic.equals(transactionsTopic)) {
                TransactionDto transactionDto = objectMapper.readValue(message, TransactionDto.class);
                Account acc = accountService.getAccount(transactionDto.getAccountId());
                if (acc.getAccountStatus().name().equals("OPEN")) {

                    Transaction transaction = Transaction.builder()
                            .accountId(acc.getAccountId())
                            .requestedTime(transactionDto.getTransactionTime())
                            .completedTime(LocalDateTime.now())
                            .amount(transactionDto.getAmount())
                            .transactionStatus(TransactionStatus.REQUESTED)
                            .build();
                    transactionService.saveTransaction(transaction);
                    acc.setBalance(acc.getBalance() + transactionDto.getAmount());
                    accountService.saveAccount(acc);
                    /*

    отправляет сообщение в топик t1_demo_transaction_accept с информацией {clientId, accountId, transactionId, timestamp, transaction.amount, account.balance}
                     */
                    TransactionAccept transactionAccept = TransactionAccept.builder()
                            .clientId(acc.getClientId())
                            .accountId(transactionDto.getAccountId())
                            .transactionId(transaction.getId())
                            .timestamp(transactionDto.getTransactionTime())
                            .transactionAmount(transactionDto.getAmount())
                            .accountBalance(acc.getBalance())
                            .build();

                    kafkaProducer.send(transactionsTopic,transactionAccept.toString());
                }
                transactionService.saveTransaction(transactionDto);
            } else {
                log.warn("Неизвестный топик: {}", topic);
            }
        } finally {
            ack.acknowledge();
        }


        log.debug("KafkaTransactionAccountConsumer consumer: записи обработаны");
    }
}
