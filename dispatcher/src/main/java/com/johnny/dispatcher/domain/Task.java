package com.johnny.dispatcher.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.johnny.dispatcher.serialise.JsonDateSerialiser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a task to perform. It runs periodically and checks whether a new
 * request should be made.
 * 
 * It's rather simplistic - in the Real World, the task, correlation and history would
 * not be combined into one class. But this just keeps things simple for now.
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
	private Long delayPeriod;
	private String cronExpression;
	
	@JsonSerialize(using=JsonDateSerialiser.class)
	private Date lastAccessed;

	public void addHistory(String message) {
		if (history == null) {
			history = new LinkedList<>();
		}
		
		history.add(message);
	}

	@JsonIgnore
	public boolean isIdle() {
		return state == null || state.equals(TaskState.IDLE);
	}

	@JsonIgnore
	public void setIdle() {
		this.state = TaskState.IDLE;
	}

	@JsonIgnore
	public boolean isWaiting() {
		return state != null && state.equals(TaskState.WAITING);
	}

	@JsonIgnore
	public void setWaiting() {
		this.state = TaskState.WAITING;
	}

	@JsonIgnore
	public boolean isTimedOut() {
		return state != null && state.equals(TaskState.TIMED_OUT);
	}

	public void updateCurrentAttempt() {
		if (currentAttempt == null) {
			currentAttempt = 0L;
		}
		
		currentAttempt++;
		if (maximumAttempts != null && currentAttempt >= maximumAttempts) {
			log.warn("Task {} has timed out", id);
			state = TaskState.TIMED_OUT;
		}
	}
	
	public void reset() {
		setIdle();
		currentAttempt = 0L;
		correlationId = null;
		lastAccessed = new Date();
	}
}
