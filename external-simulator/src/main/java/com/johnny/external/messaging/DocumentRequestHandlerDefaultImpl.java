package com.johnny.external.messaging;

import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;
import com.johnny.external.service.DocumentService;

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

/**
 * Default implementation to process a document request.
 * 
 * @author johnny
 *
 */
@Service
@Profile({ "default", "junit", "apollo", "demo" })
public class DocumentRequestHandlerDefaultImpl implements DocumentRequestHandler {

    private final DocumentService documentService;
    private final JmsTemplate jmsTemplate;
    private final MessageConverter converter;

    @Value("${document.queue.response}")
    private String documentResponseQueue;

    @Autowired
    public DocumentRequestHandlerDefaultImpl(final DocumentService documentService, final JmsTemplate jmsTemplate,
            final MessageConverter converter) {
        this.documentService = documentService;
        this.jmsTemplate = jmsTemplate;
        this.converter = converter;
    }

    @Override
    public void processDocumentReqest(final DocumentRequest documentRequest) {
        final Document document = documentService.findDocument(documentRequest.getDocument().getId());
        documentRequest.setDocument(document);

        final MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(final Session session) throws JMSException {
                return converter.toMessage(documentRequest, session);
            }
        };

        jmsTemplate.send(documentResponseQueue, messageCreator);
    }

}
