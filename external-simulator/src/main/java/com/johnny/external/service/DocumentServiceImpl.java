package com.johnny.external.service;

import com.johnny.external.dao.DocumentDAO;
import com.johnny.external.domain.Document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("defaultDocumentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDAO documentDAO;

    @Override
    public Document findDocument(final long id) {
        return documentDAO.find(id);
    }

    @Override
    public Document findDocumentByTitle(final String title) {
        return documentDAO.findByTitle(title);
    }

    @Override
    public List<Document> findAllDocuments() {
        return documentDAO.findAll();
    }

    @Override
    public void updateDocument(final Document document) {
        documentDAO.update(document);
    }

    @Override
    public void deleteDocument(final Document document) {
        documentDAO.delete(document);
    }

}
