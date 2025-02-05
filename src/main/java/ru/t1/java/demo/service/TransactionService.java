package ru.t1.java.demo.service;

import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.model.Transaction;

public interface TransactionService {

    Transaction getTransaction(Long id);
}
