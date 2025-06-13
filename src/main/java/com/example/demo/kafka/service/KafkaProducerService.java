package com.example.demo.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.adm.dtos.CreateUserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaProducerService {
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessage(String topic, String msg) {
		try {
			CreateUserDto request = new CreateUserDto();
			request.setUsername("admin");
			request.setPassword("admin");
			kafkaTemplate.send("hello-worker", "user-info", request); // topic, key, value
		} catch (Exception ex) {
			log.info(ex.getMessage());
		}
	}
}
