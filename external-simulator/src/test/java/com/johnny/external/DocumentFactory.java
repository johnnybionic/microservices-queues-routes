package com.johnny.external;

import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;

/**
 * Methods to support testing.
 * 
 * @author johnny
 *
 */
public class DocumentFactory {

    private static final Long DOCUMENT_ID = 1l;
    private static final String IDENTIFIER = "ID-1-1-1";

    public static Document getDocumentIdOnly() {

        return new Document(DOCUMENT_ID, null, null);
    }

    public static DocumentRequest getDocumentRequest() {
        final DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, getDocumentIdOnly(), null);
        return documentRequest;
    }
}
