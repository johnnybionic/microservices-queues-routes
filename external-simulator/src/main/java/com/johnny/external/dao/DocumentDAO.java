package com.johnny.external.dao;

import com.johnny.external.domain.Document;

/**
 * This module uses native JDBC - so everything needs to be done 'by hand'.
 * 
 */
public interface DocumentDAO extends GenericDAO<Document> {

    /**
     * Find by title.
     * 
     * @param title the title
     * @return the {@link Document}
     */
    Document findByTitle(String title);
}
