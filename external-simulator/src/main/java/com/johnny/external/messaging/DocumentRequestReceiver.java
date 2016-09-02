package com.johnny.external.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.johnny.external.config.ActiveMQConfiguration;
import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class DocumentRequestReceiver {

	public static final String ACKNOWLEDGED = "Acknowledged";
	private DocumentRequestHandler handler;
	
	@Autowired
	public DocumentRequestReceiver (DocumentRequestHandler handler) {
		this.handler = handler;
	}

	/**
	 * The conversion is handled by {@link DocumentRequestConverter}, which is registered 
	 * in {@link ActiveMQConfiguration}. 
	 * 
	 * Note that the method should return immediately, so that the acknowledgement is sent immediately to
	 * the requester. A better way to do this would be a Camel route that would send the acknowledgement and 
	 * then continue processing to provide the actual response via DocumentRequestHandler. Otherwise, DocumentRequestHandler
	 * can start a worker thread to process the response.
	 *  
	 * @param documentRequest
	 * @return
	 */
	//@JmsListener(destination = "document.request.queue")
	@JmsListener(destination = "${document.queue}")
	@SendTo("${document.queue.ack}")
	public String receiveDocumentRequest(DocumentRequest documentRequest) {
		log.info(documentRequest);
		handler.processDocumentReqest(documentRequest);
		return ACKNOWLEDGED;
	}
	
}
