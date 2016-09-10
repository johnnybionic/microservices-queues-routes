package com.johnny.external.exception;

/**
 * Used when the requested document is not found.
 * 
 * @author johnny
 *
 */
public class DocumentNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DocumentNotFoundException(final String message) {
        super(message);
    }

}
