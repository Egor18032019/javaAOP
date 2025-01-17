package ru.t1.java.demo.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private long accountId;
    private double amount;
    private LocalDateTime transactionTime;
}
