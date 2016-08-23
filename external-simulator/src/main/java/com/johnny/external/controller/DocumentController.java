package com.johnny.external.controller;

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

import com.johnny.external.domain.Document;
import com.johnny.external.exception.DocumentNotFoundException;
import com.johnny.external.service.DocumentService;

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
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public Document getDocumentById(@PathVariable Long id) {
		
		Document document = documentService.findDocument(id);
		return document;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Collection<Document> getAllDocuments() {
		
		Collection<Document> documents = documentService.findAllDocuments();
		return documents;
	}

	@RequestMapping(path = "/title/{title}", method = RequestMethod.GET)
	public Document getDocumentByTitle(@PathVariable String title) {
		
		Document document = documentService.findDocumentByTitle(title);
		return document;
	}

	/**
	 * Controller-specific handler

	 * @throws IOException 
	 */
	@ExceptionHandler(DocumentNotFoundException.class)
	public void documentNotFoundExceptionHandler(DocumentNotFoundException exception, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
	}

}
