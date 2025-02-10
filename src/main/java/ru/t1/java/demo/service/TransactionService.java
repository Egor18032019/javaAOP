package ru.t1.java.demo.service;


import ru.t1.java.demo.dto.TransactionDto;

public interface TransactionService {

    TransactionDto getTransaction(Long id);

    Long createTransaction(TransactionDto transactionDto);

    void saveTransaction(TransactionDto transactionDto);

}
