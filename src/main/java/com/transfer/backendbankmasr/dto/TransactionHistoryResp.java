// TransactionHistoryResp.java
package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionHistoryResp {
    private Long transactionId;
    private Long userId;
    private String description;
    private Double amount;
    private String date;

    // Constructors, getters, setters
    public TransactionHistoryResp(Long transactionId, Long userId, String description, Double amount, String date) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Getters and setters...
}

