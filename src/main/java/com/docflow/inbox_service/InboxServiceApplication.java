package com.docflow.inbox_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Inbox Microservice Documentation",
				description = "DocFlow Inbox Microservice REST API Documentation",
				version = "v1"
		)
)
@EnableJpaAuditing
public class InboxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InboxServiceApplication.class, args);
	}

}
