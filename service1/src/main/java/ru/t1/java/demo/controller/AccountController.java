package ru.t1.java.demo.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.HandlingResult;
import ru.t1.java.demo.aop.LogException;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(value = "/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService service;

    @PostMapping(value = "/create")
    public ResponseEntity createAccount(@RequestBody AccountDto account) {
        if (service.sendAccountToKafka(account)) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.badRequest().build();

    }


    @LogException
    @HandlingResult
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String id) throws InterruptedException {
        Account account = service.getAccount(UUID.fromString(id));
        AccountDto accountDto = new AccountDto(account.getAccountId(), account.getClientId(), account.getAccountType(), account.getBalance(), account.getAccountStatus());
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable UUID id,
                                                    @RequestBody AccountDto dto) {
        Account account = service.update(id, dto).orElseThrow(() -> new RuntimeException("not found"));
        AccountDto accountDto = new AccountDto(account.getAccountId(), account.getClientId(), account.getAccountType(), account.getBalance(), account.getAccountStatus());

        return ResponseEntity.ok(accountDto);
    }

}
