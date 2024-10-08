package com.transfer.backend.controller;

import com.transfer.backend.dto.RegisterUserRequest;
import com.transfer.backend.dto.RegisterUserResponse;
import com.transfer.backend.exception.response.ErrorDetails;
import com.transfer.backend.security.JwtService;
import com.transfer.backend.dto.LoginRequestDTO;
import com.transfer.backend.dto.LoginResponseDTO;
import com.transfer.backend.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Register new User")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterUserResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    public RegisterUserResponse register(@RequestBody @Valid  RegisterUserRequest user) {
        return this.authService.register(user);

    }
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
            return this.authService.login(loginRequestDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtService.invalidateToken(token);  // Invalidate the token server-side
        }
        return ResponseEntity.ok().body("User logged out successfully");
    }
}
