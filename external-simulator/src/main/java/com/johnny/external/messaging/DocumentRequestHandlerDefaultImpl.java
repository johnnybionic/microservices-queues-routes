package com.johnny.external.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;
import com.johnny.external.service.DocumentService;

@Service
@Profile({"default", "junit", "apollo", "demo"})
public class DocumentRequestHandlerDefaultImpl implements DocumentRequestHandler {
	
	private DocumentService documentService;
	private JmsTemplate jmsTemplate;
	private MessageConverter converter;

	@Value("${document.queue.response}")
	private String documentResponseQueue;
	
	
	@Autowired
	public DocumentRequestHandlerDefaultImpl(DocumentService documentService, JmsTemplate jmsTemplate, MessageConverter converter) {
		this.documentService = documentService;
		this.jmsTemplate = jmsTemplate;
		this.converter = converter;
	}
	
	@Override	
	public void processDocumentReqest(DocumentRequest documentRequest) {
		Document document = documentService.findDocument(documentRequest.getDocument().getId());
		documentRequest.setDocument(document);
		
		MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return converter.toMessage(documentRequest, session);
            }
        };
        
        jmsTemplate.send(documentResponseQueue, messageCreator);
	}

}
