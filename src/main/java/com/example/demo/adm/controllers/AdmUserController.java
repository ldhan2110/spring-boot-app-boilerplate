package com.example.demo.adm.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.adm.dtos.CreateUserDto;
import com.example.demo.adm.services.AdmUserService;
import com.example.demo.common.utils.CommonFunction;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Operations related to user management")
public class AdmUserController {
	@Autowired
	private AdmUserService service;

	@PostMapping("createUser")
	public ResponseEntity<?> createUser(@RequestBody CreateUserDto request) {
		HashMap<String, Object> response = new HashMap<>();
		try {
			service.createNewUser(request);
			response.put("Success", true);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			response.put("ERROR_CODE", "COM0000");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/secure")
	public String secureEndpoint() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return "Secure info: " + CommonFunction.getUserId();
	}
}