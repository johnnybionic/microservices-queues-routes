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

/**
 * Configures Apollo MQ. The application can switch between ActiveMQ and Apollo
 * by using profiles.
 * 
 * @author johnny
 *
 */
@Configuration
@Profile("apollo")
public class ApolloMQConfiguration {

    private static final int DEFAULT_PORT = 61616;

    @Autowired
    private MessageConverter documentConverter;

    private ConnectionFactory connectionFactory;

    /**
     * Configures Apollo with default settings. TODO: Needs to be softened.
     * 
     * @param url the URL of the server
     * @return a {@link ConnectionFactory}
     */
    @Bean
    public ConnectionFactory mqConnectionFactory(@Value("${activemq.url}") final String url) {
        final ConnectionFactoryImpl factory = new ConnectionFactoryImpl("localhost", DEFAULT_PORT, "admin", "password");
        this.connectionFactory = factory;
        return factory;
    }

    /**
     * Something else complicated.
     * 
     * @param cf connection factory
     * @return a {@link DefaultJmsListenerContainerFactory}
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(final ConnectionFactory cf) {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(documentConverter);
        return factory;
    }

    /**
     * Creates a connection to the server.
     * 
     * @return a connection
     * @throws JMSException if the connection is not possible.
     */
    @Bean
    public Connection createConnection() throws JMSException {
        final Connection connection = connectionFactory.createConnection("admin", "password");
        connection.start();
        return connection;
    }
}
