package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final KafkaProducer kafkaProducer;
    @Value("${t1.kafka.topic.t1_demo_accounts}")
    private String topic;


    @Override
    @Metric(value = 1L)
    @LogDataSourceError
    public Account getAccount(UUID id) {
        return repository.findByAccountId(id);
    }

    @Override
    @Metric(1L)
    @LogDataSourceError
    public void sendAccountToKafka(AccountDto dto) {
        Account account = Account.builder()
                .clientId(dto.getClientId())
                .accountType(dto.getAccountType())
                .balance(dto.getBalance())
                .accountStatus(dto.getAccountStatus())
                .build();
        kafkaProducer.send(dto, topic);

    }

    @Override
    @Metric(2L)
    @LogDataSourceError
    public Optional<Account> update(UUID id, AccountDto dto) {

        return Optional.ofNullable(repository.findById(id).map(account -> {
            account.setAccountType(dto.getAccountType());
            account.setBalance(dto.getBalance());
            return repository.save(account);
        }).orElseThrow(() -> new RuntimeException("Error updating account with id: " + id)));
    }

    @Override
    @Metric(1L)
    @LogDataSourceError
    public boolean delete(UUID id) {
        return repository.findById(id).map(account -> {
            repository.delete(account);
            return true;
        }).orElseThrow((() -> new RuntimeException("Error delete account with id: " + id)));
    }

    @Override
    public void saveAccountDto(AccountDto accountDto) {
        Account account = Account.builder()
                .clientId(accountDto.getClientId())
                .accountType(accountDto.getAccountType())
                .balance(accountDto.getBalance())
                .accountStatus(accountDto.getAccountStatus())
                .build();
        repository.save(account);
    }

    @Override
    public void saveAccount(Account account) {
        repository.save(account);
    }
}

