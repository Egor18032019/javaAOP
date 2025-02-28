package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.java.demo.model.Account;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByAccountId(UUID accountId);
}
