package com.example.demo.security.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
	@Value("${jwt.access-token-secret}")
	private String secretKey;

	@Value("${jwt.refresh-token-secret}")
	private String refreshSecretKey;

	@Value("${jwt.expireTime}")
	private Integer expireTime;

	private final long refreshExpireTime = 2592000000L;

	public String generateToken(String username) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expireTime.intValue())).signWith(key).compact();
	}

	public String generateRefreshToken(String username) {
		SecretKey key = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpireTime)).signWith(key).compact();
	}

	public Claims extractClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Claims extractClaimsForRefreshToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public String extractUsernameForRefreshToken(String token) {
		return extractClaimsForRefreshToken(token).getSubject();
	}

	public boolean isTokenExpiredForRefreshToken(String token) {
		return extractClaimsForRefreshToken(token).getExpiration().before(new Date());
	}

	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}
	
	public boolean validateRefreshToken(String token) {
		try {
			String extracted = extractUsernameForRefreshToken(token);
			boolean expired = isTokenExpiredForRefreshToken(token);
			return extracted != null && !expired;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public boolean validateToken(String token, String username) {
		try {
			String extracted = extractUsername(token);
			boolean expired = isTokenExpired(token);
			return extracted.equals(username) && !expired;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
}