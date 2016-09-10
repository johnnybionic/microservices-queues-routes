package com.johnny.external.controller;

import com.johnny.external.domain.Document;
import com.johnny.external.exception.DocumentNotFoundException;
import com.johnny.external.service.DocumentService;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * The {@link Document} class is annotated with @XmlRootElement. Use
 * "Accept: application/xml" to get XML returned.
 * 
 * @author johnny
 *
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * Retrieve a document by its ID.
     * 
     * @param id the ID
     * @return the document, else null.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public final Document getDocumentById(@PathVariable final Long id) {

        Document document = documentService.findDocument(id);
        return document;
    }

    /**
     * Retrieve all documents.
     * 
     * @return a collection of documents
     */
    @RequestMapping(method = RequestMethod.GET)
    public final Collection<Document> getAllDocuments() {

        Collection<Document> documents = documentService.findAllDocuments();
        return documents;
    }

    /**
     * Retrieve a document by its title.
     * 
     * @param title the title
     * @return the document, else null
     */
    @RequestMapping(path = "/title/{title}", method = RequestMethod.GET)
    public final Document getDocumentByTitle(@PathVariable final String title) {

        Document document = documentService.findDocumentByTitle(title);
        return document;
    }

    /**
     * Controller-specific handler.
     * 
     * @param exception the exception that occurred
     * @param response the current {@link HttpServletResponse}
     * @throws IOException in case things go really bad
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    public final void documentNotFoundExceptionHandler(final DocumentNotFoundException exception,
            final HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
