package com.johnny.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@Profile("junit")
public class HazelcastConfig {

	@Bean
	public HazelcastInstance hazelcastInstance() {
		return Mockito.mock(HazelcastInstance.class);
	}
}
