package com.example.demo.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dtos.LoginRequestDto;
import com.example.demo.auth.dtos.LoginResponseDto;
import com.example.demo.auth.dtos.RefreshTokenRequestDto;
import com.example.demo.auth.dtos.RefreshTokenResponseDto;
import com.example.demo.auth.services.AuthService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService service;

	@PostMapping("login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
		return ResponseEntity.ok(service.authenticate(request));
	}
	
	@PostMapping("refresh")
	public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {
		return ResponseEntity.ok(service.generateAccessToken(request));
	}

}
