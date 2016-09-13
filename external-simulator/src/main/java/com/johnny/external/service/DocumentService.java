package com.johnny.external.service;

import com.johnny.external.domain.Document;

import java.util.List;

/**
 * Service layer for documents.
 * 
 * @author johnny
 *
 */
public interface DocumentService {

    /**
     * Find by ID.
     * 
     * @param id the ID
     * @return the document
     */
    Document findDocument(long id);

    /**
     * Find by title.
     * 
     * @param title the title
     * @return the document
     */
    Document findDocumentByTitle(String title);

    /**
     * Find all.
     * 
     * @return the collection of documents
     */
    List<Document> findAllDocuments();

    /**
     * Update.
     * 
     * @param document the document to update
     */
    void updateDocument(Document document);

    /**
     * Delete.
     * 
     * @param document the document to delete
     */
    void deleteDocument(Document document);

}
