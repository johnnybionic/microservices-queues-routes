package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.DocumentRequest;
import com.johnny.dispatcher.service.RunTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This simple implementation brokers a single action for the response.
 * 
 * @author johnny
 *
 */
@Component
public class DocumentResponseHandlerDefault implements DocumentResponseHandler {

    private DocumentResponseAction action;
    private RunTaskService runTaskService;

    @Autowired
    public DocumentResponseHandlerDefault(final DocumentResponseAction action, final RunTaskService runTaskService) {
        this.action = action;
        this.runTaskService = runTaskService;
    }

    @Override
    public void processDocumentResponse(final DocumentRequest documentRequest) {
        action.perform(documentRequest);
        runTaskService.markAsComplete(documentRequest.getIdentifier());
    }

}
