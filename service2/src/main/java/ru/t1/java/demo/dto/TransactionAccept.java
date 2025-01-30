package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAccept {
//    {clientId, accountId, transactionId, timestamp, transaction.amount, account.balance}

    private Long clientId;
    private UUID accountId;
    private UUID transactionId;
    private LocalDateTime timestamp;

    @JsonProperty("transaction_amount")
    private Double transactionAmount;
    @JsonProperty("account_balance")
    private Double accountBalance;
}
