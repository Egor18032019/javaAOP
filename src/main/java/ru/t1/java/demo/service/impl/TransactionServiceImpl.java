package ru.t1.java.demo.service.impl;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository repository;
    private final KafkaProducer kafkaProducer;


    @Value("${t1.kafka.topic.t1_demo_transactions}")
    private String topic;

    @Override
    @Metric(1L)
    @LogDataSourceError
    public Transaction getTransaction(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Long createTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .amount(transactionDto.getAmount())
                .transactionTime(transactionDto.getTransactionTime())
                .build();
        kafkaProducer.send(transactionDto, topic);
        return transaction.getId();
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .transactionTime(transactionDto.getTransactionTime())
                .amount(transactionDto.getAmount())
                .build();
        repository.save(transaction);
    }
}
