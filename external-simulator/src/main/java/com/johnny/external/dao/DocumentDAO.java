package com.johnny.external.dao;

import com.johnny.external.domain.Document;

/**
 *  This module uses native JDBC - so everything needs to be done 'by hand'.
 * 
 */
public interface DocumentDAO extends GenericDAO<Document> {

	// additional method(s) to basic CRUD
	Document findByTitle(String title);
}
