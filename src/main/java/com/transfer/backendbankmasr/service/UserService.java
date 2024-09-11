package com.transfer.backendbankmasr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transfer.backendbankmasr.dto.*;
import com.transfer.backendbankmasr.entity.UserEntity;
import com.transfer.backendbankmasr.exception.custom.EmailAlreadyUsedException;
import com.transfer.backendbankmasr.exception.custom.NameAlreadyUsedException;
import com.transfer.backendbankmasr.exception.custom.PasswordMismatchException;
import com.transfer.backendbankmasr.exception.custom.ResourceNotFoundException;
import com.transfer.backendbankmasr.repository.UserRepository;
import com.transfer.backendbankmasr.security.JwtService;
import com.transfer.backendbankmasr.service.utilities.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper ;
    @Autowired
    private Utility utils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtService jwtService;

    public Long getUserIdByUsername(String username) {
        Optional<UserEntity> user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return user.get().getUserId();
    }
    @Override
    public UserDTO getUserById(long userId) throws JsonProcessingException {
        String redisKey = "userid:" + userId;
        // Try to get the user data as a JSON string from Redis
        String cashedUser = redisTemplate.opsForValue().get(redisKey);
        if (cashedUser != null) {
            // Deserialize the JSON back to UserDTO
            UserEntity user = objectMapper.readValue(cashedUser, UserEntity.class);
            return user.toDTO();
        }
        // Retrieve from database if not available in Redis
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Convert to DTO for return
        UserDTO userDTO = user.toDTO();
        // Serialize the UserEntity to JSON and store it in Redis
        cashedUser = objectMapper.writeValueAsString(user);
        redisTemplate.opsForValue().set(redisKey, cashedUser, 30, TimeUnit.MINUTES);
        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> user.toDTO())
                .collect(Collectors.toList());
    }

    @Override
    public UserTokenDTO updateUser(Long userId, UpdateUserReq req) throws JsonProcessingException {
        String redisKey = "userid:" + userId;
        String cashedUser = redisTemplate.opsForValue().get(redisKey);
        UserEntity user;
        if (cashedUser != null) {
            // Deserialize the JSON back to UserDTO
             user = objectMapper.readValue(cashedUser, UserEntity.class);
        }
        else {
             user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        // Check if the email already exists, but exclude the current user's email
        if (!user.getEmail().equals(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyUsedException("The email " + req.getEmail() + " is already in use");
        }


        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());

        // Save the updated user
        user = userRepository.save(user);
        UserDTO userDTO = user.toDTO();

        // Update/Invalidate Redis cache
       utils.updateRedisCache(user, redisKey, false);
       // Set removeOnly to false if you want to update the cache
        // Update Security Context
        // Generate new JWT and refresh token
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        String newJwtToken = jwtService.generateToken(new HashMap<>(), userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId(user.getUserId());
        userTokenDTO.setName(user.getUsername());
        userTokenDTO.setEmail(user.getEmail());
        userTokenDTO.setCreatedAt(user.getCreatedAt());
        userTokenDTO.setUpdatedAt(user.getUpdatedAt());
        userTokenDTO.setAccessToken(newJwtToken);
//        userTokenDTO.setRefreshToken(newRefreshToken);
        // Update Redis cache with new tokens
        redisTemplate.opsForValue().set("token:" + userId, newJwtToken, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("refreshToken:" + userId, newRefreshToken, 30, TimeUnit.MINUTES);

        // Update Security Context with new authentication
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return userTokenDTO;

    }



    public void changePassword(Long userId,ChangePasswordRequest request) throws JsonProcessingException {
        String redisKey = "userid:" + userId;
        String cashedUser = redisTemplate.opsForValue().get(redisKey);
        UserEntity user;
        if (cashedUser != null) {
            // Deserialize the JSON back to UserDTO
            user = objectMapper.readValue(cashedUser, UserEntity.class);
        }
        else {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            utils.updateRedisCache(user, redisKey, false);
        }
         if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Wrong password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) throws JsonProcessingException {
        String redisKey = "userid:" + userId;
        String cashedUser = redisTemplate.opsForValue().get(redisKey);
        UserEntity user;
        if (cashedUser != null) {
            // Deserialize the JSON back to UserDTO
            user = objectMapper.readValue(cashedUser, UserEntity.class);
            utils.updateRedisCache(user, redisKey, true);

        }
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }
}
