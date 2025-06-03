package com.example.demo.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.demo.adm.dtos.CreateUserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
	 // Generic method to create consumer factory for any type
    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> valueType) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Configure JsonDeserializer to trust your package
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        return new DefaultKafkaConsumerFactory<>(
            config, 
            new StringDeserializer(), 
            new JsonDeserializer<>(valueType)
        );
    }
    
    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createListenerContainerFactory(Class<T> valueType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(valueType));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, exception) -> {
            System.err.println("[Kafka-consumer] ERROR " + exception + " " + record);
        }));
        return factory;
    }
    
    // Specific beans for your Services
    @Bean("createUserDtoConsumerFactory")
    ConsumerFactory<String, CreateUserDto> createUserDtoConsumerFactory() {
        return createConsumerFactory(CreateUserDto.class);
    }

    @Bean("createUserDtoKafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, CreateUserDto> createUserDtoKafkaListenerContainerFactory() {
        return createListenerContainerFactory(CreateUserDto.class);
    }
}
