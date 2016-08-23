package com.johnny.dispatcher.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
//@Profile("!junit")
public class ActiveMQConfiguration {

	@Autowired
	private MessageConverter documentConverter;
	
    /** Provide a connection factory for ActiveMQ (may not be required as Spring has a default) */
    @Bean
    public ConnectionFactory mqConnectionFactory(@Value("${activemq.url}") String url) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(documentConverter);
        return factory;
    }

}
