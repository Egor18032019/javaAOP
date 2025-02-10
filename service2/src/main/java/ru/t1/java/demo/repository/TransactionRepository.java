package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Transaction findByTransactionId(UUID transactionId);
    int countByAccountIdAndTimestampBetween(UUID accountId, LocalDateTime start, LocalDateTime end);

    List<Transaction> findByAccountIdAndTimestampBetween(UUID accountId, LocalDateTime start, LocalDateTime end);

}
