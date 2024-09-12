package com.transfer.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transfer.backend.dto.UpdateUserReq;
import com.transfer.backend.dto.UserDTO;
import com.transfer.backend.dto.UserTokenDTO;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(long userId) throws JsonProcessingException;
    UserTokenDTO updateUser(Long userId, UpdateUserReq req) throws JsonProcessingException;
    void deleteUserById(Long userId) throws JsonProcessingException;

}
