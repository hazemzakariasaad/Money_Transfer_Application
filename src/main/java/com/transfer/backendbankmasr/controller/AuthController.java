package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.RegisterUserRequest;
import com.transfer.backendbankmasr.dto.RegisterUserResponse;
import com.transfer.backendbankmasr.exception.response.ErrorDetails;
import com.transfer.backendbankmasr.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new User")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RegisterUserResponse.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    public RegisterUserResponse register(@RequestBody @Valid RegisterUserRequest user)  {
        return this.authService.register(user);
    }
}
