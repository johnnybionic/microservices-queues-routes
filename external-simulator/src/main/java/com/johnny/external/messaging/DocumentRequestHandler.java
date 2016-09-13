package com.johnny.external.messaging;

import com.johnny.external.domain.DocumentRequest;

/**
 * Handle document requests from messaging (e.g. when a request arrives from
 * ActiveMQ)
 * 
 * @author johnny
 *
 */
public interface DocumentRequestHandler {

    /**
     * Process a request.
     * 
     * @param documentRequest the incoming request
     */
    void processDocumentReqest(DocumentRequest documentRequest);
}
