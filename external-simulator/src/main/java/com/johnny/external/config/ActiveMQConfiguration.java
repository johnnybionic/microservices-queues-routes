package com.johnny.external.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * Configuration for ActiveMQ.
 * 
 * @author johnny
 *
 */
@Configuration
public class ActiveMQConfiguration {

    @Autowired
    private MessageConverter documentConverter;

    /**
     * Provide a connection factory for ActiveMQ (may not be required as Spring
     * has a default).
     * 
     * @param url the URL of the ActiveMQ server
     * @return the factory
     */
    @Bean
    public ConnectionFactory mqConnectionFactory(@Value("${activemq.url}") final String url) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        return factory;
    }

    /**
     * Provides the {@link JmsListenerContainerFactory}.
     * 
     * @param connectionFactory incoming factory
     * @return the factory
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(final ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(documentConverter);
        return factory;
    }

}
