package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.CreateUserReq;
import com.transfer.backendbankmasr.dto.CreateUserResp;
import com.transfer.backendbankmasr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResp> createUser(@RequestBody CreateUserReq req) {
        CreateUserResp createdUser = userService.createUser(req);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserResp> getUserById(@PathVariable Long id) {
        CreateUserResp user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        List<CreateUserResp> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.ok("No users found");
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateUserResp> updateUser(@PathVariable Long id, @RequestBody CreateUserReq req) {
        CreateUserResp updatedUser = userService.updateUser(id, req);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
