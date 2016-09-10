package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.DocumentRequest;

public interface DocumentResponseAction {

	void perform(DocumentRequest documentRequest);
}
