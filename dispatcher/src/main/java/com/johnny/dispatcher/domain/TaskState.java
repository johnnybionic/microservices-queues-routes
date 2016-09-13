package com.johnny.dispatcher.domain;

/**
 * Defines the states a {@link Task} can be in.
 * 
 * @author johnny
 *
 */
public enum TaskState {

    IDLE, RUNNING, TIMED_OUT, SUSPENDED
}
