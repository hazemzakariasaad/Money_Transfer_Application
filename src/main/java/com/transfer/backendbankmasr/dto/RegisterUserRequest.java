package com.transfer.backendbankmasr.dto;

import com.transfer.backendbankmasr.enums.Country;
import jakarta.persistence.Enumerated;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterUserRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Enumerated()
    private Country country;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "confirmPassword cannot be empty")
    private String confirmPassword;

    @NotEmpty(message = "Date of birth cannot be empty")
    private LocalDate dateOfBirth;
}
