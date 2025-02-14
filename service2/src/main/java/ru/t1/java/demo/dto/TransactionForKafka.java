package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.util.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionForKafka {
    @JsonProperty("transaction_id")
    private UUID transactionId;

    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty( "amount")
    private double amount;
    @JsonProperty("completed_time")
    private LocalDateTime completedTime;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
}
