package ru.t1.java.demo.service;

import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.model.Account;

public interface AccountService {
    @LogDataSourceError
    Account getAccount(Long id);
}
