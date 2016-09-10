package com.johnny.dispatcher.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * Configures ActiveMQ. The application can switch between ActiveMQ and Apollo
 * by using profiles.
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
     * @param url autowired URL of ActiveMQ server
     * @return a {@link ConnectionFactory}
     */
    @Bean
    public ConnectionFactory mqConnectionFactory(@Value("${activemq.url}") final String url) {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        return factory;
    }

    /**
     * Something else complicated.
     * 
     * @param connectionFactory connection factory
     * @return a {@link DefaultJmsListenerContainerFactory}
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(final ConnectionFactory connectionFactory) {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(documentConverter);
        return factory;
    }

}
