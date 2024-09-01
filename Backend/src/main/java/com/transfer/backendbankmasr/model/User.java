package com.transfer.backendbankmasr.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
@Entity
@Table(name="Users")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long userId;
    private String username;
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    private String password;
    private String confirmPassword;
    @NotEmpty(message = "Date cannot be empty")
    private LocalDate dateOfBirth;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

}
