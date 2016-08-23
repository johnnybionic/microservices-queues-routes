package com.johnny.external.service;

import java.util.List;

import com.johnny.external.domain.Document;

/**
 * Service layer for documents.
 * 
 * @author johnny
 *
 */
public interface DocumentService {

	Document findDocument(long id);
	Document findDocumentByTitle(String title);
	List<Document> findAllDocuments();
	void updateDocument(Document document);
	void deleteDocument(Document document);
	
}
