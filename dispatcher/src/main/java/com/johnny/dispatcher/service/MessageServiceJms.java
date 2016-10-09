package com.johnny.dispatcher.service;

import com.johnny.dispatcher.domain.DocumentRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import lombok.Setter;

/**
 * Sends the document request to the one-and-only request queue.
 * 
 * @author johnny
 *
 */
@Service
public class MessageServiceJms implements MessageService {

    private final JmsTemplate jmsTemplate;
    private final MessageConverter converter;

    @Setter
    @Value("${document.queue.request}")
    private String documentRequestQueue;

    @Autowired
    public MessageServiceJms(final JmsTemplate jmsTemplate, final MessageConverter converter) {

        this.jmsTemplate = jmsTemplate;
        this.converter = converter;
    }

    @Override
    public void sendMessage(final DocumentRequest documentRequest) {

        final MessageCreator messageCreator = (session) -> {
            return converter.toMessage(documentRequest, session);
        };

        jmsTemplate.send(documentRequestQueue, messageCreator);

    }
}
