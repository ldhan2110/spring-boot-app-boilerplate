package com.example.demo.auth.services;

import java.util.concurrent.CompletableFuture;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public UserInfo getUserInfoList() throws Exception {
		try {
			// Start the clock
			long start = System.currentTimeMillis();

			// Kick of multiple, asynchronous lookups
			CompletableFuture<UserInfo> page1 = CompletableFuture.supplyAsync(() -> authMapper.loadUserByUsername("admin"));
			CompletableFuture<UserInfo> page2 = CompletableFuture.supplyAsync(() -> authMapper.loadUserByUsername("damsa"));
			CompletableFuture<UserInfo> page3 = CompletableFuture.supplyAsync(() -> authMapper.loadUserByUsername("chuongle"));

			// Wait until they are all done
			CompletableFuture.allOf(page1, page2, page3).join();
			
			
//			UserInfo page1 = authMapper.loadUserByUsername("admin");
//			UserInfo page2= authMapper.loadUserByUsername("damsa");
//			UserInfo page3 = authMapper.loadUserByUsername("chuongle");

			// Print results, including elapsed time
			log.info("Elapsed time: " + (System.currentTimeMillis() - start));
			log.info("--> " + page1.get());
			log.info("--> " + page2.get());

			return page1.get();
//			return page1;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
