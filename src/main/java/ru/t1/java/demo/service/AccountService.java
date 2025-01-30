package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    Account getAccount(UUID id);

    void sendAccountToKafka(AccountDto account);

    Optional<Account> update(Long id, AccountDto account);

    boolean delete(Long id);

    void saveAccountDto(AccountDto accountDto);
    void saveAccount(Account account);
}
