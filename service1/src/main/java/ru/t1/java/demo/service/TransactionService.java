package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionForController;
import ru.t1.java.demo.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction getTransaction(UUID id);

    boolean sendTransactionInKafka(TransactionForController transactionForController);

    Transaction saveTransactionDTO(TransactionForController transactionForController);
    void saveTransaction(Transaction transaction);

    List<Transaction>  findByAccountIdAndTimestampBetween(UUID accountId, LocalDateTime from, LocalDateTime to);
    void saveAllTransactions(List<Transaction> transactions);
}
