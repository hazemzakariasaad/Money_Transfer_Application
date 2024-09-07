// MoneyTransferResp.java
package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoneyTransferResp {
    private Long senderId;
    private Long recipientId;
    private Double amount;
    private String status;

    // Constructors, getters, setters
    public MoneyTransferResp(Long senderId, Long recipientId, Double amount, String status) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.status = status;
    }

    // Getters and setters...
}
