package com.johnny.dispatcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Instead of having @Value everywhere, load all externalised values into one
 * configuration bean.
 * 
 * Based on:
 * 
 * https://www.petrikainulainen.net/programming/spring-framework/spring-from-the
 * -trenches-injecting-property-values-into-configuration-beans/
 * 
 * @author johnny
 *
 */
@Configuration
@Getter
public class ApplicationConfig {

    @Value("${hazelcast.server.address}")
    private String hazelcasetServerAddress;

    @Value("${hazelcast.group.server}")
    private String hazelcastGroupServer;

    @Value("${hazelcast.group.password}")
    private String hazelcastGroupPassword;

}
