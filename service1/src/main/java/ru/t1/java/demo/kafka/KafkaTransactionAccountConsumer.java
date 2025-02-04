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
import ru.t1.java.demo.dto.TransactionForController;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Value("${t1.kafka.topic.t1_demo_transaction_result}")
    private String transactionsTopicResult;
    @Value("${t1.kafka.transaction.timeout}")
    private Long transactionTimeout;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = {"${t1.kafka.topic.t1_demo_accounts}",
                    "${t1.kafka.topic.t1_demo_transactions}",
                    "${t1.kafka.topic.t1_demo_transaction_result}"
            },
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload String message,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
//                         @Header(KafkaHeaders.NATIVE_HEADERS) Map<String, Object> nativeHeaders
    ) throws IOException {
        log.debug("Service 1 -> consumer: Обработка новых сообщений");

        try {
            log.info("Получено сообщение из топика {}: {}", topic, message);

            if (topic.equals(accountsTopic)) {
                AccountDto accountDto = objectMapper.readValue(message, AccountDto.class);
                accountService.saveAccountDto(accountDto);
            }
            if (topic.equals(transactionsTopic)) {
                TransactionForController transactionForController = objectMapper.readValue(message, TransactionForController.class);
                Transaction transaction = transactionService.saveTransactionDTO(transactionForController);
                Account acc = accountService.getAccount(transaction.getAccountId());
                if (acc.getAccountStatus().name().equals("OPEN")) {
                    acc.setBalance(acc.getBalance() + transaction.getAmount());
                    accountService.saveAccount(acc);

                    TransactionAccept transactionAccept = TransactionAccept.builder()
                            .clientId(acc.getClientId())
                            .accountId(transactionForController.getAccountId())
                            .transactionId(transaction.getTransactionId())
                            .timestamp(transactionForController.getTimestamp())
                            .transactionAmount(transactionForController.getAmount())
                            .accountBalance(acc.getBalance())
                            .build();

                    kafkaProducer.send(transactionAccept, transactionsTopicAccept);
                }
            }
            if (topic.equals(transactionsTopicResult)) {
                TransactionResultDto transactionResultDto = objectMapper.readValue(message, TransactionResultDto.class);
                switch (transactionResultDto.getTransactionStatus()) {
                    case ACCECPTED:
                        Transaction transaction = transactionService.getTransaction(transactionResultDto.getTransactionId());
                        transaction.setTransactionStatus(transactionResultDto.getTransactionStatus());
                        transactionService.saveTransaction(transaction);
                        break;
                    case REJECTED:
                        Transaction transactionForRejected = transactionService.getTransaction(transactionResultDto.getTransactionId());
                        transactionForRejected.setTransactionStatus(transactionResultDto.getTransactionStatus());
                        transactionService.saveTransaction(transactionForRejected);
                        Account acc = accountService.getAccount(transactionForRejected.getAccountId());
                        acc.setBalance(acc.getBalance() - transactionForRejected.getAmount());
                        accountService.saveAccount(acc);
                        break;
                    case BLOCKED:
                        LocalDateTime endTime = LocalDateTime.now();
                        LocalDateTime startTime = endTime.minusSeconds(transactionTimeout);
                        List<Transaction> transactions = transactionService.findByAccountIdAndTimestampBetween(transactionResultDto.getAccountId(), startTime, endTime);
                        double amountBlockedTransatcion = 0L;
                        for (Transaction transactionFromEntity : transactions) {
                            transactionFromEntity.setTransactionStatus(TransactionStatus.BLOCKED);
                            amountBlockedTransatcion += transactionFromEntity.getAmount();
                        }
                        transactionService.saveAllTransactions(transactions);
                        Account account = accountService.getAccount(transactionResultDto.getAccountId());
                        account.setBalance(account.getBalance() - amountBlockedTransatcion);
                        account.setFrozenAmount(account.getFrozenAmount() + amountBlockedTransatcion);
                        log.info("Сохранили со статусом BLOCKED " + transactions.size() + " транзакций");

                        break;
                    default:
                        log.warn("Неизвестный статус транзакции: {}", transactionResultDto.getTransactionStatus());
                }





            }
            else {
                log.warn("Неизвестный топик: {}", topic);
            }
        } finally {
            ack.acknowledge();
        }
        log.debug("KafkaTransactionAccountConsumer consumer: записи обработаны");
    }
}
