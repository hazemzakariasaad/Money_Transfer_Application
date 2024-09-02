package com.transfer.backendbankmasr.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
public class CreateUserReq {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Date of birth cannot be empty")
    private LocalDate dateOfBirth;
}
