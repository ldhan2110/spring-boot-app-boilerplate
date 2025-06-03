package com.example.demo.adm.mappers;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.adm.dtos.CreateUserDto;

@Mapper
public interface AdmUserMapper {
	public void createNewUser(CreateUserDto request);
}
