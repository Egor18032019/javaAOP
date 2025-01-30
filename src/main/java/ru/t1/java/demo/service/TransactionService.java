package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;

public interface TransactionService {

    Transaction getTransaction(Long id);

    void sendTransactionInKafka(TransactionDto transactionDto);

    Transaction saveTransactionDTO(TransactionDto transactionDto);
    void saveTransaction(Transaction transaction);
}
