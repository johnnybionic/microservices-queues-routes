package com.johnny.external.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.dao.DocumentDAO;
import com.johnny.external.dao.DocumentDAOImpl;
import com.johnny.external.domain.Document;

/**
 * Tests the service unit only (using mock DAO).
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
public class DocumentServiceTest {

    private static final long DOCUMENT_ID = 1;
    private static final String DOCUMENT_TITLE = "title";
    private static final String DOCUMENT_CONTENT = "content";

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentDAOImpl mockDocumentDAO;

    private Document document;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        document = new Document(DOCUMENT_ID, DOCUMENT_TITLE, DOCUMENT_CONTENT);
    }

    @Test
    public void thatDocumentFoundById() {

        when(mockDocumentDAO.find(DOCUMENT_ID)).thenReturn(document);

        Document returned = documentService.findDocument(DOCUMENT_ID);
        assertNotNull(returned);
        assertEquals(DOCUMENT_ID, returned.getId(), 0);

        verify(mockDocumentDAO).find(DOCUMENT_ID);
    }

    @Test
    public void thatDocumentFoundByTitle() {

        when(mockDocumentDAO.findByTitle(DOCUMENT_TITLE)).thenReturn(document);

        Document returned = documentService.findDocumentByTitle(DOCUMENT_TITLE);

        assertNotNull(returned);
        assertEquals(DOCUMENT_ID, returned.getId(), 0);

        verify(mockDocumentDAO).findByTitle(DOCUMENT_TITLE);
    }

    @Test
    public void thatAllDocumentsFound() {
        List<Document> list = new ArrayList<>();
        list.add(document);
        list.add(new Document(7L, "title 7", "content 7"));
        when(mockDocumentDAO.findAll()).thenReturn(list);

        List<Document> findAllDocuments = documentService.findAllDocuments();
        assertTrue(findAllDocuments.size() == list.size());

        verify(mockDocumentDAO).findAll();
    }

    @Test
    public void thatDocumentIsUpdated() {
        documentService.updateDocument(document);
        verify(mockDocumentDAO).update(document);
    }

    @Test
    public void thatDocumentIsDeleted() {
        documentService.deleteDocument(document);
        verify(mockDocumentDAO).delete(document);
    }
}
