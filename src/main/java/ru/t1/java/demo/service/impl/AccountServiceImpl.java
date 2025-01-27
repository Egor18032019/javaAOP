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
    public Account getAccount(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    @Metric(1L)
    @LogDataSourceError
    public Long createAccount(AccountDto dto) {
        Account account = Account.builder()
                .clientId(dto.getClientId())
                .accountType(dto.getAccountType())
                .balance(dto.getBalance())
                .build();
        kafkaProducer.send(dto, topic);
        return account.getId();
    }

    @Override
    @Metric(2L)
    @LogDataSourceError
    public Optional<Account> update(Long id, AccountDto dto) {

        return Optional.ofNullable(repository.findById(id).map(account -> {
            account.setAccountType(dto.getAccountType());
            account.setBalance(dto.getBalance());
            return repository.save(account);
        }).orElseThrow(() -> new RuntimeException("Error updating account with id: " + id)));
    }

    @Override
    @Metric(1L)
    @LogDataSourceError
    public boolean delete(Long id) {
        return repository.findById(id).map(account -> {
            repository.delete(account);
            return true;
        }).orElseThrow((() -> new RuntimeException("Error delete account with id: " + id)));
    }

    @Override
    public void saveAccount(AccountDto accountDto) {
        Account account = Account.builder()
                .clientId(accountDto.getClientId())
                .accountType(accountDto.getAccountType())
                .balance(accountDto.getBalance())
                .build();
        repository.save(account);
    }
}

