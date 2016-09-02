package com.johnny.dispatcher.service;

import com.johnny.dispatcher.domain.DocumentRequest;

public interface MessageService {

	void sendMessage(DocumentRequest documentRequest);
}
