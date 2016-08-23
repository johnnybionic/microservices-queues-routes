package com.johnny.external.exception;

public class DocumentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DocumentNotFoundException(String message) {
		super(message);
	}


}
