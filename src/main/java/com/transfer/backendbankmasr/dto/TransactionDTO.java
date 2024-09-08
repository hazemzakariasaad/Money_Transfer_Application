package com.transfer.backendbankmasr.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private long transactionId;
    private String AccountNumber;
    private double amount;
    private String recipientName;
    private LocalDateTime transactionDate;
    private String status;
}
