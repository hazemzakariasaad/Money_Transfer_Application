package com.transfer.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDTO {

private String token;
private long id ;
private String email;
private String username;
private String message;
private HttpStatus status;

}
