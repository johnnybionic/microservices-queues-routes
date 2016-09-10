package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.config.ActiveMQConfiguration;
import com.johnny.dispatcher.domain.DocumentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Receives responses from the external system, and passes them along for
 * processing.
 * 
 * @author johnny
 *
 */
@Slf4j
@Component
public class DocumentResponseReceiver {

    private DocumentResponseHandler documentResponseHandler;

    @Autowired
    public DocumentResponseReceiver(final DocumentResponseHandler documentResponseHandler) {
        this.documentResponseHandler = documentResponseHandler;
    }

    /**
     * The conversion is handled by {@link DocumentRequestConverter}, which is
     * registered in {@link ActiveMQConfiguration}.
     * 
     * @param documentRequest the incoming document response
     */
    @JmsListener(destination = "${document.queue.response}")
    public void receiveDocumentRequest(final DocumentRequest documentRequest) {
        log.info("Receiving response for identifier [{}], document ID [{}], name '{}'", documentRequest.getIdentifier(),
                documentRequest.getDocument().getId(), documentRequest.getDocument().getTitle());
        documentResponseHandler.processDocumentResponse(documentRequest);
    }

}
