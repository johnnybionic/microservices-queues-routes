package com.johnny.dispatcher.service;

/**
 * Retrieves a correlation ID for a task.
 * 
 * @author johnny
 *
 */
public interface CorrelationIdService {

	String getCorrelationId(Long taskId);
}
