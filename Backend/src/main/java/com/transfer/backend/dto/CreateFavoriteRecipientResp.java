// Backend/src/main/java/com/transfer/backendbankmasr/dto/CreateFavoriteRecipientResp.java
package com.transfer.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFavoriteRecipientResp {

    private Long id;
    private String recipientName;
    private String recipientAccountNumber;

    public CreateFavoriteRecipientResp(Long id, String recipientName, String recipientAccountNumber) {
        this.id = id;
        this.recipientName = recipientName;
        this.recipientAccountNumber = recipientAccountNumber;
    }
}
