package com.transfer.backendbankmasr.service;


import com.transfer.backendbankmasr.dto.LoginRequestDTO;
import com.transfer.backendbankmasr.dto.LoginResponseDTO;
import com.transfer.backendbankmasr.dto.RegisterUserRequest;
import com.transfer.backendbankmasr.dto.RegisterUserResponse;

public interface IAuthService {
    RegisterUserResponse register(RegisterUserRequest registerUserRequest);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
