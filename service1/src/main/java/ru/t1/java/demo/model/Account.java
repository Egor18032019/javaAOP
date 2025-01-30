package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.t1.java.demo.util.AccountStatus;
import ru.t1.java.demo.util.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account  {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID accountId;

    @Column(name = "client_id")
    private long clientId;
    @Column(name = "account_type")
    private AccountType accountType;
    @Column(name = "balance")
    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;


    @Column(name = "frozen_amount")
    private BigDecimal frozenAmount;//todo какой тип в итоге ?
}


