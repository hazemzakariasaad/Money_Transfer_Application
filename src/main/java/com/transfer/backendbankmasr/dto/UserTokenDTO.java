package com.transfer.backendbankmasr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserTokenDTO extends UserDTO {
    private String accessToken;
    private String refreshToken;
}
