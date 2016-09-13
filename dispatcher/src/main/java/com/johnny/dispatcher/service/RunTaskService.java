package com.johnny.dispatcher.service;

/**
 * Implementations of this are called by the schedule threads to perform the
 * actual task.
 * 
 * @author johnny
 *
 */
public interface RunTaskService {

    /**
     * Run the task.
     * 
     * @param taskId the task's ID
     */
    void run(Long taskId);

    /**
     * Mark as complete, e.g. when a response is received for the task.
     * 
     * @param taskId the task's ID.
     */
    void markAsComplete(Long taskId);

    /**
     * Mark as complete, e.g. when a response is received for the task.
     * 
     * @param identifier the identifier of the task (e.g. the correlation ID
     *            sent with the request).
     */
    void markAsComplete(String identifier);
}
