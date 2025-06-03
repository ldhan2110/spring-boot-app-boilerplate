package com.example.demo.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.dtos.LoginRequestDto;
import com.example.demo.auth.dtos.LoginResponseDto;
import com.example.demo.auth.dtos.RefreshTokenRequestDto;
import com.example.demo.auth.dtos.RefreshTokenResponseDto;
import com.example.demo.auth.entities.UserInfo;
import com.example.demo.auth.mappers.AuthMapper;
import com.example.demo.security.utils.JwtUtils;

@Service
public class AuthService implements UserDetailsService {
	@Autowired
	private AuthMapper authMapper;

	@Lazy
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userInfo = authMapper.loadUserByUsername(username);
		if (userInfo == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return userInfo;
	}

	public LoginResponseDto authenticate(LoginRequestDto request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		LoginResponseDto response = new LoginResponseDto();
		response.setAccessToken(jwtUtils.generateToken(request.getUsername()));
		response.setRefreshToken(jwtUtils.generateRefreshToken(request.getUsername()));
		return response;
	}

	@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
	public RefreshTokenResponseDto generateAccessToken(RefreshTokenRequestDto request) {
		String refreshToken = request.getRefreshToken();
		if (jwtUtils.validateRefreshToken(refreshToken)) {
			String username = jwtUtils.extractUsernameForRefreshToken(refreshToken);
			String newAccessToken = jwtUtils.generateToken(username);
			RefreshTokenResponseDto response = new RefreshTokenResponseDto();
			response.setAccessToken(newAccessToken);
			return response;
		}
		return null;
	}

}
