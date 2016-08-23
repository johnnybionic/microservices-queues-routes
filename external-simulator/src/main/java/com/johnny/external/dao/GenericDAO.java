package com.johnny.external.dao;

import java.util.List;

/**
 * This module uses native JDBC - so everything needs to be done 'by hand'.
 * 
 * Basic CRUD operations.
 * 
 * @author johnny
 *
 * @param <T> the type of the entity
 */

public interface GenericDAO<T> {

	void add(T t);
	T find(Long id);
	List<T> findAll();
	void update(T t);
	void delete(T t);
	Long count();
}
