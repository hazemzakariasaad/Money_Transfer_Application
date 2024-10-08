package com.transfer.backend.service;

import com.transfer.backend.dto.LoginRequestDTO;
import com.transfer.backend.dto.LoginResponseDTO;
import com.transfer.backend.dto.RegisterUserRequest;
import com.transfer.backend.dto.RegisterUserResponse;
import com.transfer.backend.entity.AccountEntity;
import com.transfer.backend.entity.UserEntity;
import com.transfer.backend.enums.AccountCurrency;
import com.transfer.backend.enums.AccountStatus;
import com.transfer.backend.enums.AccountType;
import com.transfer.backend.exception.custom.AuthenticationFailureException;
import com.transfer.backend.exception.custom.EmailAlreadyUsedException;
import com.transfer.backend.exception.custom.IncorrectCredentialsException;
import com.transfer.backend.exception.custom.PasswordMismatchException;
import com.transfer.backend.exception.custom.*;
import com.transfer.backend.repository.UserRepository;
import com.transfer.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
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


        try {
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
        }catch (MailException e) {
            // Handle the exception (e.g., log the error, alert the user)
            System.err.println("no such email: " + e.getMessage());
            // You can also log this using a logger or handle it in a more specific way
            throw new PasswordMismatchException("no such mail");
        }



    }
//email

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Invalidate previous tokens in Redis before generating a new one
            invalidatePreviousTokens(userDetails.getUsername());

            String jwt = jwtService.generateToken(userDetails);

            Optional<UserEntity> user = userRepository.findUserByEmail(userDetails.getUsername());
            if (!user.isPresent()) {
                throw new ResourceNotFoundException("User not found");
            }

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

    private void invalidatePreviousTokens(String username) {
        // Delete any existing access and refresh tokens from Redis
        redisTemplate.delete("token:" + username);
        redisTemplate.delete("refresh_token:" + username);
    }
}