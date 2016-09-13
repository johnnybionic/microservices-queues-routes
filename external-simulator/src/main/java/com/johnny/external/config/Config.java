package com.johnny.external.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration.
 * 
 * @author johnny
 *
 */
@Configuration
public class Config {

    // --------------------------------------------
    // how to create beans for different profiles but same name
    /**
     * Gets the {@link ObjectMapper} for unit tests.
     * 
     * @return the mapper
     */
    @Bean(name = "objectMapper")
    @Profile("junit")
    public ObjectMapper junitMapper() {
        return new ObjectMapper();
    }

    /**
     * Gets the {@link ObjectMapper} for normal use.
     * 
     * @return the mapper
     */
    @Bean(name = "objectMapper")
    @Profile("!junit")
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
    // --------------------------------------------
}
