package com.johnny.external.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.Document;
import com.johnny.external.exception.DocumentNotFoundException;
import com.johnny.external.service.DocumentService;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
public class DocumentControllerTest {

    private static final Long DOCUMENT_ID = 1L;
    private static final String DOCUMENT_TITLE = "title";
    private static final String DOCUMENT_CONTENT = "content";

    @InjectMocks
    private DocumentController controller;

    @Mock
    private DocumentService mockService;

    private Document document;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        document = new Document(DOCUMENT_ID, DOCUMENT_TITLE, DOCUMENT_CONTENT);
    }

    @Test
    public void thatDocumentFoundById() {

        when(mockService.findDocument(DOCUMENT_ID)).thenReturn(document);
        Document documentById = controller.getDocumentById(DOCUMENT_ID);

        assertTrue(documentById.equals(document));
        verify(mockService).findDocument(DOCUMENT_ID);
    }

    @Test
    public void thatAllDocumentsFound() {

        List<Document> list = new ArrayList<>();
        list.add(document);
        list.add(new Document(7L, "seven", "seven"));

        when(mockService.findAllDocuments()).thenReturn(list);
        Collection<Document> documents = controller.getAllDocuments();

        assertNotNull(documents);
        assertTrue(documents.size() == 2);

        verify(mockService).findAllDocuments();
    }

    @Test
    public void thatDocumentFoundByTitle() {

        when(mockService.findDocumentByTitle(DOCUMENT_TITLE)).thenReturn(document);
        Document documentByTitle = controller.getDocumentByTitle(DOCUMENT_TITLE);
        assertTrue(documentByTitle.equals(document));
        verify(mockService).findDocumentByTitle(DOCUMENT_TITLE);

    }

    @Test
    public void thatExceptionHandlerWorks() throws IOException {
        String message = "Ooops!";
        DocumentNotFoundException exception = new DocumentNotFoundException(message);
        HttpServletResponse response = mock(HttpServletResponse.class);

        controller.documentNotFoundExceptionHandler(exception, response);

        verify(response).sendError(HttpStatus.NOT_FOUND.value(), message);
    }
}
