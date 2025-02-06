package ru.t1.java.demo.service;


import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;

import java.util.Optional;

public interface AccountService {

    Account getAccount(Long id);

    Long createAccount(AccountDto account);

    Optional<Account> update(Long id, AccountDto account);

    boolean delete(Long id);

    void saveAccount(AccountDto accountDto);

}
