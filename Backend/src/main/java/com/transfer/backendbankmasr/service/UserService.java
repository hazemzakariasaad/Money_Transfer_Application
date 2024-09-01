package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.CreateUserReq;
import com.transfer.backendbankmasr.dto.CreateUserResp;
import com.transfer.backendbankmasr.entity.User;
import com.transfer.backendbankmasr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public CreateUserResp createUser(CreateUserReq req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setDateOfBirth(req.getDateOfBirth());
        User savedUser = userRepository.save(user);
        return new CreateUserResp(savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public CreateUserResp getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new CreateUserResp(user.getUserId(), user.getUsername(), user.getEmail());
    }

    public List<CreateUserResp> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new CreateUserResp(user.getUserId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public CreateUserResp updateUser(Long userId, CreateUserReq req) {
        return userRepository.findById(userId).map(user -> {
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());
            user.setPassword(req.getPassword());
            user.setDateOfBirth(req.getDateOfBirth());
            User updatedUser = userRepository.save(user);
            return new CreateUserResp(updatedUser.getUserId(), updatedUser.getUsername(), updatedUser.getEmail());
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }
}
