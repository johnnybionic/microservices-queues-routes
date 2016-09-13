package com.johnny.hazelcast.service;

import java.util.Map;

/**
 * Defines a service to access the queue storage.
 * 
 * @author johnny
 *
 * @param <K> the ID type - specifies the position in the queue
 * @param <V> the value type
 */
public interface QueueService<K, V> {

    /**
     * Retrieve all entries for a queue.
     * 
     * @param queueName the name of the queue
     * @return a map of all entries
     */
    Map<K, V> findAll(String queueName);

    /**
     * Retrieve a single entry for a single queue.
     * 
     * @param queueName the name
     * @param id the id of the entry
     * @return the value
     */
    V findOne(String queueName, K id);
}
