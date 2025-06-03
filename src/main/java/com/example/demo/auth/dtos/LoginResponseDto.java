package com.example.demo.auth.dtos;

import lombok.Data;

@Data
public class LoginResponseDto {
	String accessToken;
	String refreshToken;
}
