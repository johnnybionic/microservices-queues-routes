package com.johnny.dispatcher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.johnny.dispatcher.domain.Document;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Hazelcast implementation.
 * 
 * @author johnny
 *
 */
@Slf4j
@Service
public class QueueServiceHazelcast implements QueueService {

    private final HazelcastInstance hazelcastInstance;
    private final ObjectMapper objectMapper;

    @Autowired
    public QueueServiceHazelcast(final HazelcastInstance hazelcastInstance, final ObjectMapper objectMapper) {
        this.hazelcastInstance = hazelcastInstance;
        this.objectMapper = objectMapper;
    }

    @Override
    public Document readQueue(final String queueName) {
        final IQueue<Object> queue = hazelcastInstance.getQueue(queueName);
        if (queue.size() > 0) {
            final Object poll = queue.poll();
            if (poll instanceof String) {
                try {
                    final Document document = objectMapper.readValue((String) poll, Document.class);
                    return document;
                }
                catch (final IOException e) {
                    log.error("Could not deserialise [{}] read from queue '{}'", poll, queueName);
                    return null;
                }
            }

            log.error("Invalid type {} read from queue '{}'", poll.getClass().getName(), queueName);
        }

        log.debug("Reading '{}' returned nothing", queueName);
        return null;
    }

    @Override
    public boolean hasEntry(final String queueName) {
        final IQueue<Object> queue = hazelcastInstance.getQueue(queueName);
        return queue.size() > 0;
    }

}
