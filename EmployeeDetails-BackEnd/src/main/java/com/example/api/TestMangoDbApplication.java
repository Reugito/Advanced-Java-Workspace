package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import lombok.extern.slf4j.Slf4j;

@EnableMongoRepositories(basePackages = "com.example.api.repository")
@SpringBootApplication
@Slf4j
public class TestMangoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestMangoDbApplication.class, args);
		log.info("Application is Running By log");
	}
	
	 @Bean
     public MethodValidationPostProcessor methodValidationPostProcessor() {
          return new MethodValidationPostProcessor();
     }

}
