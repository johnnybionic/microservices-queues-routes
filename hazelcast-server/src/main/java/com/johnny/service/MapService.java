package com.johnny.service;

import java.util.Map;

/**
 * Service layer for access to the map store. It's intended
 * for use by a front end monitor UI, hence is read-only.
 * 
 * @author johnny
 *
 */
public interface MapService<K, V> {

	Map<K, V> findAll();
	V findOne(K id);

}
