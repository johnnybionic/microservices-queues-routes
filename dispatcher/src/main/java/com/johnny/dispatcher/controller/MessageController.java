package com.johnny.dispatcher.controller;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Sends messages to MQ to test it's working.
 * 
 * @author johnny
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    private final JmsTemplate jmsTemplate;
    private final MessageConverter converter;

    @Value("${document.queue.request}")
    private String documentRequestQueue;

    @Autowired
    public MessageController(final JmsTemplate jmsTemplate, final MessageConverter converter) {
        this.jmsTemplate = jmsTemplate;
        this.converter = converter;
    }

    /**
     * Send x number of messages.
     * 
     * @param amount the number to send
     */
    @RequestMapping("/send/{amount}")
    public void sendMessages(@PathVariable final Long amount) {
        final Document document = new Document(1L, null, null);
        final DocumentRequest documentRequest = new DocumentRequest("1", document, null);

        final MessageCreator messageCreator = (session) -> {
            return converter.toMessage(documentRequest, session);
        };

        for (long counter = 0; counter < amount; counter++) {
            documentRequest.setIdentifier(String.valueOf(counter + 1));
            jmsTemplate.send(documentRequestQueue, messageCreator);
        }

    }
}
