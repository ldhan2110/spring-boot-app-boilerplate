package com.example.demo.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.common.dtos.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponseDto TodoException(Exception ex, WebRequest request) {
		log.error("[ERROR]" + "[" + request.getClass().getName() + "]:" + ex.getMessage());
		return new ErrorResponseDto("COM00001", "Something went wrong.", ex.getMessage());
	}
}
