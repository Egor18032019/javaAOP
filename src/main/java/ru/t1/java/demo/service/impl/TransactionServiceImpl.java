package ru.t1.java.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository repository;

    @Override
    @LogDataSourceError
    public Transaction getTransaction(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
