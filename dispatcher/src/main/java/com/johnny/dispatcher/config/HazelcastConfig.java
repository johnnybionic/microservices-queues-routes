package com.johnny.dispatcher.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for Hazelcast.
 * 
 * @author johnny
 *
 */
@Configuration
// a separate configuration is used for tests, see equivalent class
@Profile("!junit")
public class HazelcastConfig {

    private final ApplicationConfig applicationConfig;

    @Autowired
    public HazelcastConfig(final ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    /**
     * Creates an instance of a {@link HazelcastClient}.
     * 
     * @return {@link HazelcastInstance}
     */
    @Bean
    public HazelcastInstance hazelcastInstance() {
        final ClientConfig clientConfig = getClientConfig();
        clientConfig.getNetworkConfig().addAddress(applicationConfig.getHazelcasetServerAddress());

        final GroupConfig groupConfig = clientConfig.getGroupConfig();
        groupConfig.setName(applicationConfig.getHazelcastGroupServer());
        groupConfig.setPassword(applicationConfig.getHazelcastGroupPassword());

        final HazelcastInstance instance = getHazelcastInstance(clientConfig);
        return instance;
    }

    /**
     * Gets the {@link HazelcastInstance}. Overridable for test/fake.
     * 
     * @param clientConfig the config
     * @return the instance
     */
    protected HazelcastInstance getHazelcastInstance(final ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    /**
     * Gets the {@link ClientConfig}. Overridable for test/fake.
     * 
     * @return the config
     */
    protected ClientConfig getClientConfig() {
        return new ClientConfig();
    }
}
