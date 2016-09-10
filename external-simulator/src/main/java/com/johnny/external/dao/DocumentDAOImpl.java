package com.johnny.external.dao;

import com.johnny.external.domain.Document;
import com.johnny.external.exception.DocumentNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * An implementation that uses basic JDBC.
 * 
 * @author johnny
 *
 */
@Slf4j
@Repository
// @Profile("!junit")
public class DocumentDAOImpl implements DocumentDAO {

    private static final int POSITION_3 = 3;
    private static final int POSITION_2 = 2;
    private static final int POSITION_1 = 1;
    private static final String DOCUMENT_TABLE_NAME = "Document";
    private static final String ID_COLUMN_NAME = "id";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String CONTENT_COLUMN_NAME = "content";
    private static final String PREPARED_INSERT = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
            DOCUMENT_TABLE_NAME, ID_COLUMN_NAME, TITLE_COLUMN_NAME, CONTENT_COLUMN_NAME);
    private static final String PREPARED_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            DOCUMENT_TABLE_NAME, TITLE_COLUMN_NAME, CONTENT_COLUMN_NAME, ID_COLUMN_NAME);
    private static final String PREPARED_DELETE = String.format("DELETE FROM %s WHERE %s = ?", DOCUMENT_TABLE_NAME,
            ID_COLUMN_NAME);

    @Autowired
    private DataSource dataSource;

    @Override
    public void add(final Document document) {
        try (Connection connection = dataSource.getConnection()) {

            log.info("Adding document: {}", document);

            // prepared statement doesn't need quotes, could be cached if
            // thread-safe
            final PreparedStatement preparedStatement = connection.prepareStatement(PREPARED_INSERT);
            preparedStatement.setLong(POSITION_1, document.getId());
            preparedStatement.setString(POSITION_2, document.getTitle());
            preparedStatement.setString(POSITION_3, document.getContent());
            final int executeUpdate = preparedStatement.executeUpdate();
            Assert.isTrue(executeUpdate == 1);
        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public Document find(final Long id) {

        return findBy(String.format("SELECT * FROM %s WHERE %s = %d", DOCUMENT_TABLE_NAME, ID_COLUMN_NAME, id));
    }

    @Override
    public Document findByTitle(final String title) {

        return findBy(String.format("SELECT * FROM %s WHERE %s = '%s'", DOCUMENT_TABLE_NAME, TITLE_COLUMN_NAME, title));
    }

    /**
     * (It's a private method. Why does Checkstyle think it needs javadoc?)
     * Common method for finding a {@link Document}
     * 
     * @param sql the SQL to find the document
     * @return a {@link Document}
     * @throws DocumentNotFoundException if the document does not exist
     */
    private Document findBy(final String sql) {
        log.info("Finding by: {}", sql);
        Document document = null;

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                document = resultSetToDocument(resultSet);
            }
            else {
                // This should return null instead - and also not the actual SQL
                final String message = String.format("There was no document found for [%s]", sql);
                log.error(message);
                throw new DocumentNotFoundException(message);
            }

        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }

        return document;
    }

    @Override
    public List<Document> findAll() {

        final List<Document> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DOCUMENT_TABLE_NAME)) {

            while (resultSet.next()) {
                list.add(resultSetToDocument(resultSet));
            }

        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }

        return list;
    }

    @Override
    public void update(final Document document) {
        try (Connection connection = dataSource.getConnection()) {

            log.info("Updating document: {}", document);

            final PreparedStatement preparedStatement = connection.prepareStatement(PREPARED_UPDATE);
            preparedStatement.setString(POSITION_1, document.getTitle());
            preparedStatement.setString(POSITION_2, document.getContent());
            preparedStatement.setLong(POSITION_3, document.getId());
            final int executeUpdate = preparedStatement.executeUpdate();
            Assert.isTrue(executeUpdate == 1);
        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(final Document document) {
        try (Connection connection = dataSource.getConnection()) {

            log.info("Deleting document: {}", document);

            final PreparedStatement preparedStatement = connection.prepareStatement(PREPARED_DELETE);
            preparedStatement.setLong(1, document.getId());
            final int executeUpdate = preparedStatement.executeUpdate();
            Assert.isTrue(executeUpdate == 1);
        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Long count() {
        long count = 0;

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {

            final ResultSet resultSet = statement
                    .executeQuery("SELECT COUNT(*) AS rowcount FROM " + DOCUMENT_TABLE_NAME);
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        }
        catch (final SQLException e) {
            log.error(e.getMessage());
        }

        return count;
    }

    private Document resultSetToDocument(final ResultSet resultSet) throws SQLException {
        return new Document(resultSet.getLong(ID_COLUMN_NAME), resultSet.getString(TITLE_COLUMN_NAME),
                resultSet.getString(CONTENT_COLUMN_NAME));
    }

}
