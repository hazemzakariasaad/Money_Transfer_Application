package com.transfer.backend.dto;

import com.transfer.backend.enums.Country;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import lombok.*;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Valid
public class RegisterUserRequest {

    @NotEmpty(message = "Username cannot be empty")
    @Length(min=3)
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
