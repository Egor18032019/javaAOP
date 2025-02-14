package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.t1.java.demo.util.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction  {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "account_id")
    private UUID accountId;
    @Column(name = "amount")
    private double amount;
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    @Column(name = "timestamp")
    private LocalDateTime transactionTime;
    @Setter
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

}
