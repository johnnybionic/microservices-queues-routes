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

    @Override
    public String getCorrelationId(final Long taskId) {
        final IMap<String, String> map = hazelcastInstance.getMap(CORRELATION_ID_MAP_NAME);

        // should always return something, but just in case
        Assert.notNull(map, "Expected map not to be null");

        // but it might not have the entry yet
        String retVal = map.get(CORRELATION_ID_MAP_KEY);
        if (retVal == null) {
            retVal = INITIAL_CORRELATION_ID;
        }

        map.put(CORRELATION_ID_MAP_KEY, nextCorrelationId.nextValue(retVal));
        return retVal;
    }

}
