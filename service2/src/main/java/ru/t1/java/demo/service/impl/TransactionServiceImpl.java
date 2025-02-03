package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionForKafka;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.time.LocalDateTime;
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

    public Transaction getTransaction(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void sendTransactionInKafka(TransactionDto transactionDto) {
        TransactionForKafka transaction = TransactionForKafka.builder()
                .accountId(transactionDto.getAccountId())
                .amount(transactionDto.getAmount())
                .timestamp(transactionDto.getTransactionTime())
                .completedTime(LocalDateTime.now())
                .build();
        kafkaProducer.send(transaction, topic);

    }

    @Override
    public Transaction saveTransactionDTO(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .timestamp(transactionDto.getTransactionTime())
                .completedTime(LocalDateTime.now())
                .amount(transactionDto.getAmount())
                .build();
        repository.save(transaction);
        return transaction;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        repository.save(transaction);
    }
}
