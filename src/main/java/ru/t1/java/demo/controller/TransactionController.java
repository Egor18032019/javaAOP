package ru.t1.java.demo.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.HandlingResult;
import ru.t1.java.demo.aop.LogException;
import ru.t1.java.demo.aop.Track;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;

@Slf4j
@RestController
@RequestMapping(value = "/transaction")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class TransactionController {
    TransactionService service;
    @Track
    @LogException
    @HandlingResult
    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) throws InterruptedException {
        Transaction transaction = service.getTransaction(id);
        return ResponseEntity.ok(transaction);
    }
}
