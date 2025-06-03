package com.example.demo.adm.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.adm.dtos.CreateUserDto;
import com.example.demo.adm.mappers.AdmUserMapper;
import com.example.demo.common.utils.CommonFunction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdmUserService {
	@Autowired
	AdmUserMapper userMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Transactional
	public void createNewUser(CreateUserDto request) throws BeansException {
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		log.info(CommonFunction.getUserId());
		userMapper.createNewUser(request);
	}
}
