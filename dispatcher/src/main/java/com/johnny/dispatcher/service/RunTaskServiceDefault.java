package com.johnny.dispatcher.service;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;
import com.johnny.dispatcher.domain.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class RunTaskServiceDefault implements RunTaskService {

    private final TaskDao dao;
    private final MessageService messageService;
    private final QueueService queueService;
    private final CorrelationIdService correlationIdService;

    @Autowired
    public RunTaskServiceDefault(final TaskDao dao, final MessageService messageService,
            final QueueService queueService, final CorrelationIdService correlationIdService) {

        this.dao = dao;
        this.messageService = messageService;
        this.queueService = queueService;
        this.correlationIdService = correlationIdService;
    }

    @Override
    public void run(final Long taskId) {

        final Task task = dao.findOne(taskId);
        if (isRunnable(task)) {
            runTask(task);
        }

    }

    @Override
    public void markAsComplete(final String identifier) {
        final Task task = dao.findByCorrelationId(identifier);
        if (isRunning(task)) {
            markAsComplete(task);
        }
    }

    @Override
    public void markAsComplete(final Long taskId) {
        final Task task = dao.findOne(taskId);
        if (isRunning(task)) {
            markAsComplete(task);
        }
    }

    private void markAsComplete(final Task task) {
        log.info("Marking task '{}' with correlation ID '{}' as complete", task.getId(), task.getCorrelationId());
        task.reset();
    }

    private boolean isRunning(final Task task) {
        if (task.isTimedOut()) {
            // don't do anything - the task timed out waiting for this response,
            // so
            // something needs to be looked into
            log.warn("Tried to mark a timed out task with id {} as complete", task.getId());
        }

        return task.isRunning();
    }

    /*
     * Run the task
     * 
     */
    private void runTask(final Task task) {

        log.info("Running task {}:{}", task.getId(), task.getName());
        task.addHistory("Attempting to run task");

        // 1: read the queue
        final Document readQueue = queueService.readQueue(task.getSourceQueue());

        // 2: get the correlation ID
        final String correlationId = correlationIdService.getCorrelationId(task.getId());
        task.setCorrelationId(correlationId);

        // 3: set the state
        task.setRunning();

        final DocumentRequest documentRequest = new DocumentRequest(correlationId, readQueue, null);

        // 4: send the request
        messageService.sendMessage(documentRequest);
        log.info("Message for task {} sent with correlation ID [{}]", task.getName(), correlationId);
        task.addHistory("Task message sent");
    }

    private boolean isRunnable(final Task task) {
        return !suspended(task) && !waiting(task) && !timedOut(task) && hasEntry(task);
    }

    private boolean suspended(final Task task) {
        if (task.isSuspended()) {
            log.debug("Task '{}' with id {} ignored as it is suspended", task.getName(), task.getId());
            return true;
        }

        return false;
    }

    private boolean hasEntry(final Task task) {
        final boolean hasEntry = queueService.hasEntry(task.getSourceQueue());
        log.debug("hasEntry() for '{}' returned {}", task.getSourceQueue(), hasEntry);
        return hasEntry;
    }

    private boolean timedOut(final Task task) {
        if (task.isTimedOut()) {
            final String message = String.format("Task [%s] not run as it has timed out", task.getName());
            log.debug(message);
            return true;
        }

        return false;
    }

    private boolean waiting(final Task task) {

        if (task.isRunning()) {
            task.updateCurrentAttempt();

            final String message = String.format("Task [%s] not run as still waiting for [%s]", task.getName(),
                    task.getCorrelationId());
            task.addHistory(message);
            log.info(message);
            return true;
        }

        return false;
    }

}
