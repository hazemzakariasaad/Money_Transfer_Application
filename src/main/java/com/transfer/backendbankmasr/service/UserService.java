package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.*;
import com.transfer.backendbankmasr.entity.User;
import com.transfer.backendbankmasr.exception.custom.EmailAlreadyUsedException;
import com.transfer.backendbankmasr.exception.custom.NameAlreadyUsedException;
import com.transfer.backendbankmasr.exception.custom.PasswordMismatchException;
import com.transfer.backendbankmasr.exception.custom.ResourceNotFoundException;
import com.transfer.backendbankmasr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;




    @Override
    public UserDTO getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.toDTO();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> user.toDTO())
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserReq req) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));  // Correctly handle the Optional<User>


            if(userRepository.existsByEmail(req.getEmail())) {
                throw new EmailAlreadyUsedException("the email" + req.getEmail() + " is already in use");

            }
            if (userRepository.existsByUsername(req.getUsername())) {
                throw new NameAlreadyUsedException(req.getUsername() +"name already in use");
            }
            user.setUsername(req.getUsername());
            user.setEmail(req.getEmail());

            // Save the updated user
            user = userRepository.save(user);

            return user.toDTO();
        }

    public void changePassword(Long userId,ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }
}
