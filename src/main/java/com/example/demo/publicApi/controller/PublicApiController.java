package com.example.demo.publicApi.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.kafka.service.KafkaProducerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicApiController {
	@Autowired
	private KafkaProducerService service;

	@PostMapping("hello")
	public ResponseEntity<HashMap<String, String>> login(@RequestBody HashMap<String, String> request) {
		HashMap<String, String> response = new HashMap<String, String>();
		service.sendMessage("hello", "Hello World");
		response.put("data", "ok");
		return ResponseEntity.ok(response);
	}
}
