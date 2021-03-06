package com.johnny.dispatcher.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.core.HazelcastInstance;

@Configuration
@Profile("junit")
public class HazelcastConfig {

	@Bean
	public HazelcastInstance hazelcastInstance() {
		return Mockito.mock(HazelcastInstance.class);
	}
}
