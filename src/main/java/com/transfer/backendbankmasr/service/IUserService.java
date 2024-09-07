package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.UpdateUserReq;
import com.transfer.backendbankmasr.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(long userId);
    UserDTO updateUser(Long userId, UpdateUserReq req);
    void deleteUserById(Long userId);

}
