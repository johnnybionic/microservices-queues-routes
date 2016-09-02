package com.johnny.dispatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;
import com.johnny.dispatcher.domain.Task;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class DefaultRunTaskService implements RunTaskService {

	private TaskDao dao;
	private MessageService messageService;
	private QueueService queueService;
	private CorrelationIdService correlationIdService;

	@Autowired
	public DefaultRunTaskService(TaskDao dao, MessageService messageService, QueueService queueService,
			CorrelationIdService correlationIdService) {

		this.dao = dao;
		this.messageService = messageService;
		this.queueService = queueService;
		this.correlationIdService = correlationIdService;
	}


	@Override
	public void run(Long taskId) {
		
		Task task = dao.findOne(taskId);
		
		log.info("Running task {}:{}", task.getId(), task.getName());
		if (isRunnable(task)) {
			runTask(task);
		}

	}

	/*
	 * Run the task
	 * 
	 */
	private void runTask(Task task) {
		
		task.addHistory("Attempting to run task");
		
		// 1: read the queue
		Document readQueue = queueService.readQueue(task.getSourceQueue());
		
		// 2: get the correlation ID
		String correlationId = correlationIdService.getCorrelationId();
		task.setCorrelationId(correlationId);
		
		// 3: set the state
		task.setWaiting();
		
		DocumentRequest documentRequest = new DocumentRequest(correlationId, readQueue, null);
		
		// 4: send the request 
		messageService.sendMessage(documentRequest);
		log.info("Message for task {} sent with correlation ID [{}]", task.getName(), correlationId);
		task.addHistory("Task message sent");
	}

	private boolean isRunnable(Task task) {
		return !waiting(task) && !timedOut(task) && hasEntry(task);
	}
	
	private boolean hasEntry(Task task) {
		boolean hasEntry = queueService.hasEntry(task.getSourceQueue());
		log.debug("hasEntry() for '{}' returned {}", task.getSourceQueue(), hasEntry); 
		return hasEntry;
	}

	private boolean timedOut(Task task) {
		if (task.isTimedOut()) {
			String message = String.format("Task [%s] not run as it has timed out", task.getName());
			log.debug(message);
			return true;
		}

		return false;
	}

	private boolean waiting(Task task) {
		
		if (task.isWaiting()) {
			task.updateCurrentAttempt();

			String message = String.format("Task [%s] not run as still waiting for [%s]", task.getName(), task.getCorrelationId());
			task.addHistory(message);
			log.info(message);
			return true;
		}
		
		return false;
	}
}
