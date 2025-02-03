package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.TransactionForController;
import ru.t1.java.demo.dto.TransactionForKafka;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final KafkaProducer kafkaProducer;


    @Value("${t1.kafka.topic.t1_demo_transactions}")
    private String topic;

    @Override
    @Metric(1L)
    @LogDataSourceError
    public Transaction getTransaction(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void sendTransactionInKafka(TransactionForController transactionForController) {
        TransactionForKafka transaction = TransactionForKafka.builder()
                .accountId(transactionForController.getAccountId())
                .amount(transactionForController.getAmount())
                .timestamp(transactionForController.getTimestamp())
                .completedTime(LocalDateTime.now())
                .build();
        kafkaProducer.send(transaction, topic);

    }

    @Override
    public Transaction saveTransactionDTO(TransactionForController transactionForController) {
        Transaction transaction = Transaction.builder()
                .accountId(transactionForController.getAccountId())
                .timestamp(transactionForController.getTimestamp())
                .completedTime(LocalDateTime.now())
                .amount(transactionForController.getAmount())
                .build();
        repository.save(transaction);
        return transaction;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        repository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountIdAndTimestampBetween(UUID accountId, LocalDateTime from, LocalDateTime to) {
        List<Transaction> transactions = repository.findByAccountIdAndTimestampBetween(accountId, from, to);
        return transactions;
    }

    @Override
    public void saveAllTransactions(List<Transaction> transactions) {
        repository.saveAll(transactions);
    }
}
