package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;
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
public class Transaction extends AbstractPersistable<Long> {
    @Column(name = "account_id")
    private long accountId;
    @Column(name = "amount")
    private double amount;
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    @Column(name = "requested_time")
    private LocalDateTime requestedTime;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @UuidGenerator
    private UUID transactionId;

}
