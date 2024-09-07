// MoneyTransferReq.java
package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoneyTransferReq {
    private Long senderId;
    private Long recipientId;
    private Double amount;

    // Constructors, getters, setters
    public MoneyTransferReq(Long senderId, Long recipientId, Double amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    // Getters and setters...
}
