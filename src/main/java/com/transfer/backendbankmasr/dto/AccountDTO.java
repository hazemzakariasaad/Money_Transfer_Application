package com.transfer.backendbankmasr.dto;

import com.transfer.backendbankmasr.enums.AccountCurrency;
import com.transfer.backendbankmasr.enums.AccountStatus;
import com.transfer.backendbankmasr.enums.AccountType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private Double balance;

    private AccountCurrency currency;

    private String accountName;

    private String accountDescription;

    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
