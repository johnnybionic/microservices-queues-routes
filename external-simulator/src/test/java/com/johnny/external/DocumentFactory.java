package com.johnny.external;

import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;

/**
 * Methods to support testing.
 * @author johnny
 *
 */
public class DocumentFactory {

	private static final Long DOCUMENT_ID = 1l;
	private static final String DOCUMENT_TITLE = "Title 1";
	private static final String DOCUMENT_CONTENT = "Content 1";
	private static final String IDENTIFIER = "ID-1-1-1";

	public static Document getDocumentIdOnly() {
		
		return new Document(DOCUMENT_ID, null, null);
	}

	public static DocumentRequest getDocumentRequest() {
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, getDocumentIdOnly(), null);
		return documentRequest;
	}
}
