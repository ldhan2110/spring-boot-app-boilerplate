package com.example.demo.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.auth.entities.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonFunction {
	public static String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserInfo userInfo = (UserInfo)authentication.getPrincipal();
    	return userInfo.getUsername();
    }

    public static UserInfo getUserInfo() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserInfo userInfo = (UserInfo)authentication.getPrincipal();
    	return userInfo;
    }

}
