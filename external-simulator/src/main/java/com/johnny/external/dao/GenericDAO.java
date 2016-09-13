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

    /**
     * Add an entity.
     * 
     * @param t the entity to add
     */
    void add(T t);

    /**
     * Find by ID.
     * 
     * @param id the ID
     * @return an entity, if found
     */
    T find(Long id);

    /**
     * Find all.
     * 
     * @return all entities.
     */
    List<T> findAll();

    /**
     * Update.
     * 
     * @param t the entity to update.
     */
    void update(T t);

    /**
     * Delete.
     * 
     * @param t the entity to delete
     */
    void delete(T t);

    /**
     * Count.
     * 
     * @return a count of the number of entries found for this entity.
     */
    Long count();
}
