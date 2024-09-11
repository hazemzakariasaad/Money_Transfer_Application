package com.transfer.backendbankmasr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.transfer.backendbankmasr.dto.*;
import com.transfer.backendbankmasr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.transfer.backendbankmasr.dto.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);  // Logger instance

    @Autowired
    private UserService userService;

    @Operation(summary = "Get user By ID", description = "Get user")
    @ApiResponse(responseCode = "200", description = "User Get successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/id")
    public UserDTO getUserById() throws IOException {
        logger.info("Fetching user by ID from authenticated context");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = userService.getUserIdByUsername(username);
        UserDTO user = userService.getUserById(userId);

        logger.debug("Fetched user: {}", user);
        return user;
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws JsonProcessingException {
        logger.info("Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            logger.debug("Fetched user: {}", user);
        } else {
            logger.warn("User with ID {} not found", id);
        }
        if (user != null) {
            return ResponseEntity.ok("No users found");
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get All users", description = "Get All users")
    @ApiResponse(responseCode = "200", description = "Users Get successfully")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.ok("No users found");
        }
        return ResponseEntity.ok(users);
    }


    @Operation(summary = "Update user", description = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "User Updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserReq req) throws JsonProcessingException {
        UserTokenDTO updatedUser = userService.updateUser(id, req);
        logger.info("Updating user with ID: {}", id);
        logger.debug("Updated user: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }


    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws JsonProcessingException {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
        logger.debug("User with ID {} deleted", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Change password for user", description = "Change password for user by ID")
    @ApiResponse(responseCode = "200", description = "User password updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/changepassword")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request)throws JsonProcessingException{
        logger.info("Changing password for user with ID: {}", id);
        userService.changePassword(id, request);
        logger.debug("Password changed for user ID {}", id);
        return ResponseEntity.ok().build();
    }
}
