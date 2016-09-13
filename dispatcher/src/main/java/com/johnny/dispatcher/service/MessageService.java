package com.johnny.dispatcher.service;

import com.johnny.dispatcher.domain.DocumentRequest;

/**
 * Sens a message to the external system.
 * 
 * @author johnny
 *
 */
public interface MessageService {

    /**
     * Send a message.
     * 
     * @param documentRequest the request to send
     */
    void sendMessage(DocumentRequest documentRequest);
}
