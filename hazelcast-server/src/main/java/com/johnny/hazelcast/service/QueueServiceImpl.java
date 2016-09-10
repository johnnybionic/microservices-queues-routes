package com.johnny.hazelcast.service;

import com.johnny.hazelcast.persistence.MySQLQueueStore;
import com.johnny.hazelcast.persistence.MySQLQueueStoreFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A simple wrapper service that allows read-only access to a queue store, for
 * instance by a monitoring application.
 * 
 * The interesting bit about this is that queue stores are prototypes. A single
 * instance of the store is used, and the name of the queue changed for each
 * call. This would not be thread-safe: if that was required, then the factory
 * could be used for each call.
 * 
 * @author johnny
 *
 */
@Service
public class QueueServiceImpl implements QueueService<Long, String> {

    private final MySQLQueueStore store;

    @Autowired
    public QueueServiceImpl(final MySQLQueueStoreFactory factory, final MySQLQueueStore store) {
        this.store = store;
    }

    @Override
    public Map<Long, String> findAll(final String queueName) {
        // QueueStore<String> store = factory.newQueueStore(queueName, null);
        store.setQueueName(queueName);
        final Set<Long> loadAllKeys = store.loadAllKeys();
        if (loadAllKeys.size() == 0) {
            return new HashMap<Long, String>();
        }

        final Map<Long, String> loadAll = store.loadAll(loadAllKeys);
        return loadAll;
    }

    @Override
    public String findOne(final String queueName, final Long id) {
        // QueueStore<String> store = factory.newQueueStore(queueName, null);
        store.setQueueName(queueName);
        final String load = store.load(id);
        return load;
    }

}
