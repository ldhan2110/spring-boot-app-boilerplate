package com.example.demo.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Application").version("1.0.0")
				.description("API documentation for your Spring Boot application")
				.contact(new Contact().name("Your Name").email("your.email@example.com").url("https://yourwebsite.com"))
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}
