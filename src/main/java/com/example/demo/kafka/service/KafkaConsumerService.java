package com.example.demo.kafka.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.example.demo.adm.dtos.CreateUserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerService {

	@KafkaListener(topics = "hello-worker", groupId = "group_id", containerFactory="createUserDtoKafkaListenerContainerFactory")
	public void consume(CreateUserDto record, Acknowledgment ack) {
		try {
			log.info("=== MESSAGE RECEIVED ===");
			log.info("Raw value: {}", record);
		} catch (Exception e) {
			log.error("Error in consumer method: ", e);
		}
	}

}
