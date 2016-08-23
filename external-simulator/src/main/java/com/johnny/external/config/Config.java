package com.johnny.external.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class Config {

	// --------------------------------------------
	// how to create beans for different profiles but same name 
	@Bean(name = "objectMapper")
	@Profile("junit")
	public ObjectMapper junitMapper () {
		return new ObjectMapper();
	}
	
	@Bean(name = "objectMapper")
	@Profile("!junit")
	public ObjectMapper Mapper () {
		return new ObjectMapper();
	}
	// --------------------------------------------
}
