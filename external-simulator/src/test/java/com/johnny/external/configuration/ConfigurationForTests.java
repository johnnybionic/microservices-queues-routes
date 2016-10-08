package com.johnny.external.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration that is specific to tests.
 * 
 * @author johnny
 *
 */
@Configuration
public class ConfigurationForTests {

    /**
     * Use a spy to test exception handling.
     * 
     * @return a spied {@link ObjectMapper}
     */
    @Bean(name = "objectMapper")
    @Profile("junit")
    public ObjectMapper junitMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return Mockito.spy(objectMapper);
    }

}
