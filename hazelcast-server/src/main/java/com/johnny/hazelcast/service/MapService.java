package com.johnny.hazelcast.service;

import java.util.Map;

/**
 * Service layer for access to the map store. It's intended for use by a front
 * end monitor UI, hence is read-only.
 * 
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author johnny
 *
 */
public interface MapService<K, V> {

    Map<K, V> findAll();

    V findOne(K id);

}
