// Backend/src/main/java/com/transfer/backendbankmasr/dto/CreateFavoriteRecipientReq.java
package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFavoriteRecipientReq {

    private String recipientName;
    private String recipientAccountNumber;
}



