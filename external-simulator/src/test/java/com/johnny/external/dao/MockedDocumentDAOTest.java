package com.johnny.external.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.Document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This version of the test uses mocks instead of an in-memory database. This
 * will allow SQLExceptions to be simulated.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
public class MockedDocumentDAOTest {

    @InjectMocks
    private DocumentDAOImpl documentDAO;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnAdd() throws SQLException {
        Document document = new Document(1l, "new document", "new content");

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.add(document);
    }

    @Test
    public void testSQLExceptionCaughtForStatementOnAdd() throws SQLException {
        Document document = new Document(1l, "new document", "new content");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
        documentDAO.add(document);
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnFind() throws SQLException {

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.find(1L);
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnFindAll() throws SQLException {

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.findAll();
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnUpdate() throws SQLException {
        Document document = new Document(1l, "new document", "new content");

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.update(document);
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnDelete() throws SQLException {
        Document document = new Document(1l, "new document", "new content");

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.delete(document);
    }

    @Test
    public void testSQLExceptionCaughtForConnectionOnCount() throws SQLException {

        when(dataSource.getConnection()).thenThrow(new SQLException());
        documentDAO.count();
    }

    @Test
    public void testSQLExceptionCaughtForStatementOnCount() throws SQLException {

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenThrow(new SQLException());
        documentDAO.count();
    }

    @Test
    public void whenNoResultsReturnedForCount() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Long count = documentDAO.count();
        assertTrue("Expected count of zero", count == 0);
    }

}
