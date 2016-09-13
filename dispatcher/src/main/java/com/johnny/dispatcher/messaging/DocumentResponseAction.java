package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.DocumentRequest;

/**
 * Defines what to do when a response for a document request is received.
 * 
 * @author johnny
 *
 */
public interface DocumentResponseAction {

    /**
     * Perform an action for the response.
     * 
     * @param documentRequest the requested document (the response from the
     *            other system)
     */
    void perform(DocumentRequest documentRequest);
}
