package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.util.AccountStatus;
import ru.t1.java.demo.util.AccountType;

import java.util.UUID;

/**
 * DTO for {@link ru.t1.java.demo.model.Account}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    @JsonProperty("account_id")
    private UUID account_id;
    @JsonProperty("client_id")
    private long clientId;
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("balance")
    private double balance;
    @JsonProperty("account_status")
    private AccountStatus accountStatus;
}
