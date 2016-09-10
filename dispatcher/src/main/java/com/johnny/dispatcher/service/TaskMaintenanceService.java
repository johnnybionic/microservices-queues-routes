package com.johnny.dispatcher.service;

import java.util.Collection;

import com.johnny.dispatcher.domain.Task;

/**
 * Methods in this interface are intended for maintenance, e.g. by a UI tool 
 */
public interface TaskMaintenanceService {
	
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

	/**
	 * Sets a task as suspended, so it no longer runs, or reinstates a
	 * suspended task. Used to stop tasks running if there's a problem.
	 * 
	 * @param id the task's ID
	 * @param action true to suspend, false to reinstate
	 */
	void suspend(Long id, Boolean action);

}
