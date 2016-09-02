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

	String nextValue(String current);
}
