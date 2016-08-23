package com.johnny.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.QueueStore;
import com.johnny.persistence.MySQLQueueStore;
import com.johnny.persistence.MySQLQueueStoreFactory;

/**
 * A simple wrapper service that allows read-only access to a queue store,
 * for instance by a monitoring application. 
 * 
 * The interesting bit about this is that queue stores are prototypes. 
 * An single instance of the store is used, and the name of the queue changed
 * for each call. This would not be thread-safe: if that was required,
 * then the factory could be used for each call.
 * 
 * @author johnny
 *
 */
@Service
public class QueueServiceImpl implements QueueService<Long, String> {

	private MySQLQueueStoreFactory factory;
	private MySQLQueueStore store;

	@Autowired
	public QueueServiceImpl(MySQLQueueStoreFactory factory, MySQLQueueStore store) {
		this.factory = factory;
		this.store = store;
	}

	@Override
	public Map<Long, String> findAll(String queueName) {
		//QueueStore<String> store = factory.newQueueStore(queueName, null);
		store.setQueueName(queueName);
		Set<Long> loadAllKeys = store.loadAllKeys();
		Map<Long, String> loadAll = store.loadAll(loadAllKeys);
		return loadAll;
	}

	@Override
	public String findOne(String queueName, Long id) {
		//QueueStore<String> store = factory.newQueueStore(queueName, null);
		store.setQueueName(queueName);
		String load = store.load(id);
		return load;
	}

}
