package com.example.demo.auth.mappers;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.auth.entities.UserInfo;

@Mapper
public interface AuthMapper {
	UserInfo loadUserByUsername(String username);
}
