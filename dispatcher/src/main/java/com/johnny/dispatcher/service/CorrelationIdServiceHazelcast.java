package com.johnny.dispatcher.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Uses a map on the HazelcastServer to store the correlation ID.
 * 
 * @author johnny
 *
 */

@Slf4j
@Service
public class CorrelationIdServiceHazelcast implements CorrelationIdService {

    public static final String CORRELATION_ID_MAP_NAME = "correlationIdStore";
    public static final String CORRELATION_ID_MAP_KEY = "correlationId";
    public static final String INITIAL_CORRELATION_ID = "1";

    private final HazelcastInstance hazelcastInstance;

    private final NextCorrelationId nextCorrelationId = (current) -> {
        return String.valueOf(Long.valueOf(current) + 1);
    };

    @Autowired
    public CorrelationIdServiceHazelcast(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    /**
     * Retrieves the correlation ID and increments the stored value. TODO:
     * Perhaps that should be two distinct steps? Might want to retrieve the
     * correlation ID multiple times. Also, this method is synchronised, as the
     * taskID is not used (there is a change two tasks could call concurrently).
     * 
     * @param taskId the task ID. It's not currently used
     * @return the id
     */
    @Override
    public synchronized String getCorrelationId(final Long taskId) {
        final IMap<String, String> map = hazelcastInstance.getMap(CORRELATION_ID_MAP_NAME);

        // should always return something, but just in case
        Assert.notNull(map, "Expected map not to be null");

        // but it might not have the entry yet
        // synchronized (this) {
        String retVal = map.get(CORRELATION_ID_MAP_KEY);
        if (retVal == null) {
            log.debug("Initial value used");
            retVal = INITIAL_CORRELATION_ID;
        }

        log.info("Retrieving correlation ID '{}' for task '{}'", retVal, taskId);
        map.put(CORRELATION_ID_MAP_KEY, nextCorrelationId.nextValue(retVal));
        return retVal;
        // }
    }

}
