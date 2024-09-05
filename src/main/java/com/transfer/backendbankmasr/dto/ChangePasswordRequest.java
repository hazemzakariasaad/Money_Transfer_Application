package com.transfer.backendbankmasr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
public class ChangePasswordRequest {
    @NotEmpty(message = "currentPassword cannot be empty")
    private String currentPassword;

    @NotEmpty(message = "newPassword cannot be empty")
    private String newPassword;

}
