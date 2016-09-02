package com.johnny.dispatcher.service;

import com.johnny.dispatcher.domain.Document;

/**
 * Reads a {@link Document} from a given queue, for example Hazelcast.
 * 
 * @author johnny
 *
 */
public interface QueueService {

	/**
	 * Checks if the queue has anything waiting. Originally 'readQueue' would
	 * return null but it didn't seem very elegant. 
	 * 
	 * @param queueName the queue to check
	 * @return true if waiting, false if not
	 */
	boolean hasEntry(String queueName);
	
	/**
	 * Returns a {@link Document} from a queue.
	 * 
	 * @param queueName the queue from which to retrieve
	 * @return the {@link Document}, or null if nothing exists
	 */
	Document readQueue(String queueName);
}
