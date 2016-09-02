package com.johnny.dispatcher.service;

import java.util.Collection;

import com.johnny.dispatcher.domain.Task;

/**
 * Methods in this interface are intended for maintenance, e.g. by a UI tool 
 */
public interface TaskService {
	
	/**
	 * Get all tasks.
	 * 
	 * @return the tasks
	 */
	Collection<Task> findAll();
	
	/**
	 * Reset a task - puts it back to IDLE with no current count.
	 * 
	 * @param taskId the task to reset
	 */
	void reset(Long taskId);

}
