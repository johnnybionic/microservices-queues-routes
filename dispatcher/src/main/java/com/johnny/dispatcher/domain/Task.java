package com.johnny.dispatcher.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.johnny.dispatcher.serialise.JsonDateSerialiser;

import java.util.Date;
//import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a task to perform. It runs periodically and checks whether a new
 * request should be made.
 * 
 * It's rather simplistic - in the Real World, the task, correlation and history
 * would not be combined into one class. But this just keeps things simple for
 * now.
 * 
 * @author johnny
 *
 */
@Entity
@Data
@NoArgsConstructor
@Slf4j
public class Task {

    @Id
    private Long id;

    private String name;
    private String sourceQueue;
    private Long maximumAttempts = 0L;
    private Long currentAttempt = 0L;

    @Enumerated(EnumType.STRING)
    private TaskState state = TaskState.IDLE;

    // the current request
    private String correlationId;

    @ElementCollection
    @CollectionTable(name = "task_history")
    private List<String> history;

    // only one of these
    private Long delayPeriod = 0L;
    private String cronExpression;

    // the data is formatted as ISO
    @JsonSerialize(using = JsonDateSerialiser.class)
    private Date lastAccessed;

    /**
     * Add a message to thr Task's history.
     * 
     * @param message the message to add
     */
    public final void addHistory(final String message) {
        if (history == null) {
            history = new LinkedList<>();
        }

        history.add(message);
    }

    /**
     * Is the task idle?
     * 
     * @return true if yes, false if not.
     */
    @JsonIgnore
    public final boolean isIdle() {
        return state == null || state.equals(TaskState.IDLE);
    }

    /**
     * Set the task as idle or not.
     */
    @JsonIgnore
    public final void setIdle() {
        this.state = TaskState.IDLE;
    }

    /**
     * Is the task running? A task is running if a request has been sent to the
     * other system, and we are awaiting a response.
     * 
     * @return true is running, false if not.
     */
    @JsonIgnore
    public final boolean isRunning() {
        return state != null && state.equals(TaskState.RUNNING);
    }

    /**
     * Set the task running as running.
     */
    @JsonIgnore
    public final void setRunning() {
        this.state = TaskState.RUNNING;
    }

    /**
     * Has the task timed out? A task times out if it is in the running state
     * for too long.
     * 
     * @return true if timed out, false is not.
     */
    @JsonIgnore
    public final boolean isTimedOut() {
        return state != null && state.equals(TaskState.TIMED_OUT);
    }

    /**
     * Is the task suspended? A task is set as suspended by a ReST call, which
     * then prevents it from running.
     * 
     * @return true if suspended, false if not.
     */
    @JsonIgnore
    public final boolean isSuspended() {
        return state != null && state.equals(TaskState.SUSPENDED);
    }

    /**
     * Sets the task as suspended or not.
     * 
     * @param action the state to set.
     */
    @JsonIgnore
    public final void setSuspended(final boolean action) {
        state = action ? TaskState.SUSPENDED : TaskState.IDLE;
    }

    /**
     * Updates the task's current attempt counter, and checks if the maximum
     * attempts has been reached.
     */
    public final void updateCurrentAttempt() {
        if (currentAttempt == null) {
            currentAttempt = 0L;
        }

        currentAttempt++;
        if (maximumAttempts != null && currentAttempt >= maximumAttempts) {
            log.warn("Task {} has timed out", id);
            state = TaskState.TIMED_OUT;
        }
    }

    /**
     * Resets the task.
     */
    public final void reset() {
        setIdle();
        currentAttempt = 0L;
        correlationId = null;
        lastAccessed = new Date();
        history = null;
    }
}
