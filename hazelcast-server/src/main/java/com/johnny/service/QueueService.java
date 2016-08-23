package com.johnny.service;

import java.util.Map;

public interface QueueService<K, V> {

	Map<K, V> findAll(String queueName);
	V findOne(String queueName, K id);
}
