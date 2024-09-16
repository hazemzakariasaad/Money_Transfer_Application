package com.transfer.backend.service;


import com.transfer.backend.dto.LoginRequestDTO;
import com.transfer.backend.dto.LoginResponseDTO;
import com.transfer.backend.dto.RegisterUserRequest;
import com.transfer.backend.dto.RegisterUserResponse;

public interface IAuthService {
    RegisterUserResponse register(RegisterUserRequest registerUserRequest);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
