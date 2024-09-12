package com.transfer.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
    @NotEmpty(message = "must have id for sender")
    private Long senderId;
    @NotEmpty(message = "must haverecipientName")
    private String recipientName;
    @NotEmpty(message = "must recipientAccountNumber")
    private String recipientAccountNumber;
    @NotEmpty(message = "must have amount")
    private Double amount;
}
