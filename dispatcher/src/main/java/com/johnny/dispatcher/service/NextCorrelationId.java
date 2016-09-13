package com.johnny.dispatcher.service;

/**
 * Defines how to increment, or get next value for, the correlation ID.
 * 
 * I did this just to use a lambda :)
 * 
 * @author johnny
 *
 */
public interface NextCorrelationId {

    /**
     * Get the next value.
     * 
     * @param current the current value
     * @return the next value
     */
    String nextValue(String current);
}
