package com.johnny.dispatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.johnny.dispatcher.domain.Document;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HazelcastQueueService implements QueueService {

	private HazelcastInstance hazelcastInstance;

	@Autowired
	public HazelcastQueueService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}
	
	@Override
	public Document readQueue(String queueName) {
		IQueue<Object> queue = hazelcastInstance.getQueue(queueName);
		if (queue.size() > 0) {
			Object poll = queue.poll();
			if (poll instanceof Document) {
				return (Document) poll;
			}
		
			log.error("Invalid type {} read from queue '{}'", poll.getClass().getName(), queueName);
		}
		
		log.debug("Reading '{}' returned nothing", queueName);
		return null;
	}

	@Override
	public boolean hasEntry(String queueName) {
		IQueue<Object> queue = hazelcastInstance.getQueue(queueName);
		return queue.size() > 0; 
	}

}
