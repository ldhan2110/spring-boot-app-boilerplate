package com.example.demo.adm.dtos;

import com.example.demo.common.dtos.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserDto extends BaseDto {
	private static final long serialVersionUID = 2776877096324484985L;
	String username;
	String password;
}