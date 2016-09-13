package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.DocumentRequest;

/**
 * Defines a handler for repsonses from the external system, i.e. when the
 * systems responds to a previous request.
 * 
 * @author johnny
 *
 */
public interface DocumentResponseHandler {

    /**
     * Process the response.
     * 
     * @param documentRequest the response from the external system
     */
    void processDocumentResponse(DocumentRequest documentRequest);

}
