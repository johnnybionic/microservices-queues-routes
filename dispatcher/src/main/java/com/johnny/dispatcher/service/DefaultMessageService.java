package com.johnny.dispatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.johnny.dispatcher.domain.DocumentRequest;

import lombok.Setter;

/**
 * Sends the document request to the one-and-only request queue.
 * 
 * @author johnny
 *
 */
@Service
public class DefaultMessageService implements MessageService {

	private JmsTemplate jmsTemplate;
	private MessageConverter converter;
	
	@Setter
	@Value("${document.queue.request}")
	private String documentRequestQueue;
	
	@Autowired
	public DefaultMessageService(JmsTemplate jmsTemplate, 
			MessageConverter converter) {
		
		this.jmsTemplate = jmsTemplate;
		this.converter = converter;
	}

	@Override
	public void sendMessage(DocumentRequest documentRequest) {

		MessageCreator messageCreator = (session) -> {
        	return converter.toMessage(documentRequest, session);
        };

        jmsTemplate.send(documentRequestQueue, messageCreator);

	}
}
