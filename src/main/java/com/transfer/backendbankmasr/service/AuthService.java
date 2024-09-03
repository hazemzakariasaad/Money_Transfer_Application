package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.RegisterUserRequest;
import com.transfer.backendbankmasr.dto.RegisterUserResponse;
import com.transfer.backendbankmasr.entity.Account;
import com.transfer.backendbankmasr.entity.User;
import com.transfer.backendbankmasr.enums.AccountCurrency;
import com.transfer.backendbankmasr.enums.AccountType;
import com.transfer.backendbankmasr.exception.custom.EmailAlreadyUsedException;
import com.transfer.backendbankmasr.exception.custom.NameAlreadyUsedException;
import com.transfer.backendbankmasr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public RegisterUserResponse register(RegisterUserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyUsedException("the email" + userRequest.getEmail() + " is already in use");

        }
        if (userRepository.existsByName(userRequest.getUsername())) {
            throw new NameAlreadyUsedException("name already in use");
        }
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        Account account = Account.builder()
                .accountBalance(0.0)
                .accountType(AccountType.SAVING)
                .accountName("Savings Account")
                .currency(AccountCurrency.EGP)
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .user(user) // Link the account to the user
                .build();


        user.getAccounts().add(account);
        user= userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response ;
    }

}
