package com.johnny.external.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.Document;
import com.johnny.external.exception.DocumentNotFoundException;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
@WebAppConfiguration
// the scripts are run before/after each test method. Ensures database is
// exactly as required, and tests won't interfere with each other
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql") })
public class DocumentDAOTest {

    private static final String UPDATED_TITLE = "updated title";

    private static final String UPDATED_CONTENT = "updated content";

    private static final long DOCUMENT_ID = 1;

    private static final String TITLE = "Document 1";

    @Autowired
    private DocumentDAO documentDAO;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void thatDocumentIsFoundById() {
        Document document = documentDAO.find(DOCUMENT_ID);
        assertNotNull(document);

        assertNotNull(document.getId());
        assertEquals(DOCUMENT_ID, document.getId(), 0);

        assertNotNull(document.getTitle());
        assertNotNull(document.getContent());

    }

    @Test(expected = DocumentNotFoundException.class)
    public void testNotFoundById() {

        documentDAO.find(DOCUMENT_ID - 1);

    }

    @Test
    public void thatAllDocumentsAreRead() {
        List<Document> all = documentDAO.findAll();
        assertNotNull(all);
        assertTrue(all.size() > 0);

        long count = documentDAO.count();
        assertEquals(all.size(), count, 0);
    }

    @Test
    public void thatDocumentIsAdded() {
        long count = documentDAO.count();

        long newId = count + 1;
        Document document = new Document(newId, "new document", "new content");
        documentDAO.add(document);

        assertTrue(documentDAO.count() == newId);

        Document newDocument = documentDAO.find(newId);
        assertNotNull(newDocument);
        assertTrue(document.getId().equals(newDocument.getId()));
        assertTrue(document.getContent().equals(newDocument.getContent()));
        assertTrue(document.getTitle().equals(newDocument.getTitle()));
    }

    @Test
    public void thatDocumentIsUpdated() {
        long count = documentDAO.count();
        Document document = documentDAO.find(DOCUMENT_ID);
        document.setContent(UPDATED_CONTENT);
        document.setTitle(UPDATED_TITLE);

        documentDAO.update(document);

        document = documentDAO.find(DOCUMENT_ID);
        assertEquals(UPDATED_CONTENT, document.getContent());
        assertEquals(UPDATED_TITLE, document.getTitle());

        assertEquals(count, documentDAO.count(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDocumentIsUpdatedButDoesNotExist() {
        Document document = documentDAO.find(DOCUMENT_ID);
        document.setId(9999L);
        documentDAO.update(document);

    }

    @Test
    public void thatDocumentIsDeleted() {
        long count = documentDAO.count();
        Document document = documentDAO.find(DOCUMENT_ID);

        documentDAO.delete(document);

        try {
            document = documentDAO.find(DOCUMENT_ID);
            fail("Found document that should have been deleted");
        }
        catch (DocumentNotFoundException exception) {

        }

        assertEquals(count - 1, documentDAO.count(), 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDocumentIsDeletedButDoesNotExist() {
        Document document = documentDAO.find(DOCUMENT_ID);
        document.setId(9999L);
        documentDAO.delete(document);
    }

    @Test
    public void thatDocumentIsFoundByTitle() {

        Document document = documentDAO.findByTitle(TITLE);
        assertNotNull(document);

        assertNotNull(document.getId());
        assertEquals(DOCUMENT_ID, document.getId(), 0);

        assertNotNull(document.getTitle());
        assertNotNull(document.getContent());

    }

    @Test
    public void thatCountReturnsCountOfDocuments() {

        Long count = documentDAO.count();
        assertTrue("Count expected", count != 0);
    }

    // -------------------
    // e x c e p t i o n s
    // -------------------
    /**
     * Exceptions isn't being handled, other than a log output. Not what would
     * happen in a real system.
     */
    @Test
    @Ignore
    public void thatExceptionHandledIfTitleIsTooBigOnAdd() {

        // expectedException.expect(SQLDataException.class);
        Document document = new Document(0L, getBigTitle(), "new content");
        documentDAO.add(document);

    }

    private String getBigTitle() {
        StringBuilder title = new StringBuilder();
        for (int index = 0; index < 48; index++) {
            title.append("*");
        }
        return title.toString();
    }

}
