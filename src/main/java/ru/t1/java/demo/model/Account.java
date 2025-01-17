package ru.t1.java.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account {
    private long clientId;
    private AccountType accountType;
    private double balance;


    /**
     * Перечисление типов счетов
     */
    public enum AccountType {
        //TODo вынести отдельно ? где то еще понадобится ?
        DEBIT,
        CREDIT
    }
}


