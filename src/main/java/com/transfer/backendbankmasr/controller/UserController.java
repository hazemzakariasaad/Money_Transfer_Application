package com.transfer.backendbankmasr.controller;

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

    @Autowired
    private UserService userService;

    @Operation(summary = "Get user By ID", description = "Get user")
    @ApiResponse(responseCode = "200", description = "User Get successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/id")
    public UserDTO getUserById() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = userService.getUserIdByUsername(username);
        UserDTO user = userService.getUserById(userId);
        return user;
    }
    @GetMapping("/test/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws IOException {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok("No users found");
        }
        return ResponseEntity.ok(user);
    }
    ///edit here

    @Operation(summary = "Get All user", description = "Get All user")
    @ApiResponse(responseCode = "200", description = "Users Get successfully")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
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
        return ResponseEntity.ok(updatedUser);
    }


    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws JsonProcessingException {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }


    @Operation(summary = "change password for user", description = "change password for user by ID")
    @ApiResponse(responseCode = "200", description = "User password updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/changepassword")
    public ResponseEntity<?>changePassword(@PathVariable Long id,@RequestBody @Valid ChangePasswordRequest request){
        userService.changePassword(id,request);
        return ResponseEntity.ok().build();
    }
}
