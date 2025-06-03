package com.example.demo.common.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public abstract class BaseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	String createUser;
	String updateUser;
	String createDate;
	String updateDate;
}