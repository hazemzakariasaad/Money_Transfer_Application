package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateUserResp {

    private long userId;
    private String username;
    private String email;

    public CreateUserResp(long userId, String username, @Email(message = "Invalid email format") @NotEmpty(message = "Email cannot be empty") String email) {
    }
}
