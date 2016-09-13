package com.johnny.dispatcher.service;

/**
 * Retrieves a correlation ID for a task.
 * 
 * @author johnny
 *
 */
public interface CorrelationIdService {

    /**
     * Gets the correlation ID for the task.
     * 
     * @param taskId the (optional) task ID
     * @return the correlation ID
     */
    String getCorrelationId(Long taskId);
}
