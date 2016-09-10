package com.johnny.scheduler.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!junit")
public class HazelcastConfig {

    private final ApplicationConfig applicationConfig;

    @Autowired
    public HazelcastConfig(final ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(applicationConfig.getHazelcasetServerAddress());

        final GroupConfig groupConfig = clientConfig.getGroupConfig();
        groupConfig.setName(applicationConfig.getHazelcastGroupServer());
        groupConfig.setPassword(applicationConfig.getHazelcastGroupPassword());

        final HazelcastInstance instance = HazelcastClient.newHazelcastClient(clientConfig);
        return instance;
    }
}
