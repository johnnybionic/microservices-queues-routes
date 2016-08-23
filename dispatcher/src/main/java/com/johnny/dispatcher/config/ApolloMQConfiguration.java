package com.johnny.dispatcher.config;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
@Profile("apollo")
public class ApolloMQConfiguration {

	@Autowired
	private MessageConverter documentConverter;
	
	private ConnectionFactory connectionFactory;

    @Bean
    public ConnectionFactory mqConnectionFactory(@Value("${activemq.url}") String url) {
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl("localhost", 61616, "admin", "password");
        this.connectionFactory = factory;
        return factory;
    }
    
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(documentConverter);
        return factory;
    }

    @Bean
    public Connection createConnection() throws JMSException {
    	Connection connection = connectionFactory.createConnection("admin", "password");
    	connection.start();
        return connection;
    }
}
