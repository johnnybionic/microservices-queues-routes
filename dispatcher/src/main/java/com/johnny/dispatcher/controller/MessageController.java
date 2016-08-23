package com.johnny.dispatcher.controller;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

/**
 * Sends messages to MQ to test it's working.
 * 
 * @author johnny
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController {

	private JmsTemplate jmsTemplate;
	private MessageConverter converter;

	@Value("${document.queue.request}")
	private String documentRequestQueue;
	
	@Autowired
	public MessageController(JmsTemplate jmsTemplate, MessageConverter converter) {
		this.jmsTemplate = jmsTemplate;
		this.converter = converter;
	}

	@RequestMapping("/send/{amount}")
	public void sendMessages(@PathVariable Long amount) {
		Document document = new Document(1L, null, null);
		DocumentRequest documentRequest = new DocumentRequest("1", document, null);
		
		MessageCreator messageCreator = (session) -> {
        	return converter.toMessage(documentRequest, session);
        };

        for (long counter = 0; counter < amount; counter++) {
        	documentRequest.setIdentifier(String.valueOf(counter + 1));
            jmsTemplate.send(documentRequestQueue, messageCreator);
        }

	}
}
