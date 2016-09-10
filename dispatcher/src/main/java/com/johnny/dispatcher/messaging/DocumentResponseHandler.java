package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.DocumentRequest;

public interface DocumentResponseHandler {

	void processDocumentResponse(DocumentRequest documentRequest);

}
