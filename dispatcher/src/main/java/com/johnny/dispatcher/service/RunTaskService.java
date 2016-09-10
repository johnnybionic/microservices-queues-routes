package com.johnny.dispatcher.service;

/**
 * Implementations of this are called by the schedule threads to
 * perform the actual task.
 * 
 * @author johnny
 *
 */
public interface RunTaskService {

	void run(Long taskId);
	void markAsComplete(Long taskId);
	void markAsComplete(String identifier);
}
