package ru.t1.java.demo.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.HandlingResult;
import ru.t1.java.demo.aop.LogException;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;


@Slf4j
@RestController
@RequestMapping(value = "/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AccountController {
    AccountService service;

    @PostMapping(value = "/create")
    public ResponseEntity<Long> createAccount(@RequestBody AccountDto account) {
        Long id = service.createAccount(account);
        return ResponseEntity.ok(id);
    }


    @LogException
    @HandlingResult
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) throws InterruptedException {
        Account account = service.getAccount(id);
        AccountDto accountDto = new AccountDto(account.getClientId(), account.getAccountType(), account.getBalance());
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id,
                                                    @RequestBody AccountDto dto) {
        Account account = service.update(id, dto).orElseThrow(() -> new RuntimeException("not found"));
        AccountDto accountDto = new AccountDto(account.getClientId(), account.getAccountType(), account.getBalance());

        return ResponseEntity.ok(accountDto);
    }

}
