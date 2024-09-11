package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.LoginRequestDTO;
import com.transfer.backendbankmasr.dto.LoginResponseDTO;
import com.transfer.backendbankmasr.dto.RegisterUserRequest;
import com.transfer.backendbankmasr.dto.RegisterUserResponse;
import com.transfer.backendbankmasr.entity.AccountEntity;
import com.transfer.backendbankmasr.entity.UserEntity;
import com.transfer.backendbankmasr.enums.AccountCurrency;
import com.transfer.backendbankmasr.enums.AccountStatus;
import com.transfer.backendbankmasr.enums.AccountType;
import com.transfer.backendbankmasr.exception.custom.*;
import com.transfer.backendbankmasr.repository.UserRepository;
import com.transfer.backendbankmasr.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EmailService emailService;

    @Transactional
    public RegisterUserResponse register( RegisterUserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyUsedException("the email" + userRequest.getEmail() + " is already in use");
        }
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        UserEntity user = UserEntity.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(this.passwordEncoder.encode(userRequest.getPassword()))
                .country(userRequest.getCountry())
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();

        AccountEntity account = AccountEntity.builder()
                .accountBalance(100000)
                .accountType(AccountType.SAVING)
                .accountName("Savings Account")
                .currency(AccountCurrency.EGP)
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .accountStatus(AccountStatus.ACTIVE)
                .user(user) // Link the account to the user
                .build();

        user.getAccounts().add(account);
        UserEntity savedUser= userRepository.save(user);

        //email
        String subject = "Registration Confirmation";
        String body = "Dear " + savedUser.getUsername() + ",\n\nThank you for registering with our service.";
        emailService.sendConfirmationEmail(savedUser.getEmail(), subject, body);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setUserId(savedUser.getUserId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        response.setMessage("Registered Successfully");
        response.setStatus(HttpStatus.ACCEPTED);
        return response ;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            Optional<UserEntity> user=userRepository.findUserByEmail(userDetails.getUsername());
            return LoginResponseDTO.builder()
                    .token(jwt)
                    .message("Login Successfully")
                    .status(HttpStatus.ACCEPTED)
                    .username(user.get().getUsername())
                    .email(userDetails.getUsername())
                    .id(user.get().getUserId())
                    .build();
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException("Incorrect email or password");
        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException("Authentication failed: " + e.getMessage(), e);
        }
    }
}