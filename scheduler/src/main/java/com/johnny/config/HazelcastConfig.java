package com.johnny.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@Profile("!junit")
public class HazelcastConfig {

	private ApplicationConfig applicationConfig;

	@Autowired
	public HazelcastConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	@Bean
	public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(applicationConfig.getHazelcasetServerAddress());

        GroupConfig groupConfig = clientConfig.getGroupConfig();
        groupConfig.setName(applicationConfig.getHazelcastGroupServer());
        groupConfig.setPassword(applicationConfig.getHazelcastGroupPassword());

        HazelcastInstance instance = HazelcastClient.newHazelcastClient(clientConfig);
        return instance;
	}
}
