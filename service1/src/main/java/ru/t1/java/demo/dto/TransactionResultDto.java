package ru.t1.java.demo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.util.TransactionStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResultDto {
    @JsonProperty("transaction_id")
    private UUID transactionId;
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
}