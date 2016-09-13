package com.johnny.dispatcher.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.domain.TaskState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This test uses an inner class to override some of the configuration. The idea
 * is to use the standard configuration to instantiate whichever implementation
 * of RunTaskService is active, and inject the mock beans from this class.
 * 
 * @author johnny
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("junit")
// @SqlGroup({
// @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
// "classpath:beforeTests.sql"),
// @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
// "classpath:afterTests.sql")
// })

public class RunTaskServiceTest {

    private static final String NAME = "Task 1";
    private static final Long ID = 1L;
    private static final String CORRELATION_ID = "123";
    private static final String QUEUE_NAME = "queue.1";

    @Autowired
    private RunTaskService runTaskService;

    @Autowired
    private TaskDao dao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private CorrelationIdService correlationIdService;

    private Task scheduleTask;

    @Before
    public void setUp() throws Exception {

        scheduleTask = new Task();
        scheduleTask.setName(NAME);
        scheduleTask.setId(ID);

        reset(dao, queueService);
    }

    /**
     * The task does not run, and the current attempt is incremented
     */
    @Test
    public void thatDoesNotRunIfWaiting() {
        scheduleTask.setCurrentAttempt(0L);
        scheduleTask.setState(TaskState.RUNNING);
        scheduleTask.setCorrelationId(CORRELATION_ID);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        assertEquals((long) 1, (long) scheduleTask.getCurrentAttempt());
    }

    /**
     * The task does not run, and the current attempt is not incremented
     */
    @Test
    public void thatDoesNotRunIfTimedOut() {
        scheduleTask.setState(TaskState.TIMED_OUT);
        scheduleTask.setCorrelationId(CORRELATION_ID);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        assertEquals((long) 0, (long) scheduleTask.getCurrentAttempt());
    }

    /**
     * The task should time out if maximum attempts reached
     */
    @Test
    public void thatTaskTimesOut() {
        scheduleTask.setCurrentAttempt(0L);
        scheduleTask.setMaximumAttempts(1L);
        scheduleTask.setState(TaskState.RUNNING);
        scheduleTask.setCorrelationId(CORRELATION_ID);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        assertTrue(scheduleTask.isTimedOut());
    }

    /**
     * The task runs but there's nothing on the queue to process - no message is
     * sent - state is idle - no counters change
     */
    @Test
    public void thatNothingHappensIfThereIsNothingToProcess() {
        scheduleTask.setCurrentAttempt(0L);
        scheduleTask.setMaximumAttempts(1L);
        scheduleTask.setState(TaskState.IDLE);
        scheduleTask.setCorrelationId(null);
        scheduleTask.setSourceQueue(QUEUE_NAME);

        when(dao.findOne(ID)).thenReturn(scheduleTask);
        when(queueService.hasEntry(QUEUE_NAME)).thenReturn(false);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        verify(queueService).hasEntry(QUEUE_NAME);
        verify(queueService, never()).readQueue(QUEUE_NAME);
        assertTrue(scheduleTask.isIdle());
        assertEquals(0L, (long) scheduleTask.getCurrentAttempt());
    }

    /**
     * The task runs and finds an entry on the queue - a correlation ID is
     * obtained - the task is updated - a message is sent
     */
    @Test
    public void thatMessageSent() {
        scheduleTask.setState(TaskState.IDLE);
        scheduleTask.setCorrelationId(null);
        scheduleTask.setSourceQueue(QUEUE_NAME);

        when(dao.findOne(ID)).thenReturn(scheduleTask);
        when(queueService.hasEntry(QUEUE_NAME)).thenReturn(true);
        final Document document = new Document(ID, null, null);
        when(queueService.readQueue(QUEUE_NAME)).thenReturn(document);
        when(correlationIdService.getCorrelationId(ID)).thenReturn(CORRELATION_ID);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        verify(queueService).hasEntry(QUEUE_NAME);
        verify(queueService, times(1)).readQueue(QUEUE_NAME);
        verify(messageService).sendMessage(isA(DocumentRequest.class));
        verify(correlationIdService).getCorrelationId(ID);

        assertTrue(scheduleTask.isRunning());
        assertEquals(CORRELATION_ID, scheduleTask.getCorrelationId());
    }

    /**
     * Mark as complete - try to reset a task that isn't running
     */
    @Test
    public void thatMarkAsCompleteForNonRunningHasNoEffect() {

        scheduleTask.setState(TaskState.IDLE);
        scheduleTask.setCorrelationId(null);
        scheduleTask.setSourceQueue(QUEUE_NAME);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.markAsComplete(ID);

        verify(dao).findOne(ID);

    }

    /**
     * Mark as complete - try to reset a task that timed out
     */
    @Test
    public void thatMarkAsCompleteForTimedOutHasNoEffect() {

        scheduleTask.setState(TaskState.TIMED_OUT);
        scheduleTask.setCorrelationId(CORRELATION_ID);
        scheduleTask.setSourceQueue(QUEUE_NAME);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.markAsComplete(ID);

        verify(dao).findOne(ID);
        assertEquals(CORRELATION_ID, scheduleTask.getCorrelationId());
    }

    /**
     * Mark as complete - reset a running task - reset correlation ID - reset
     * history - set as idle
     */
    @Test
    public void thatMarkAsCompleteForRunningTaskWorks_ById() {

        scheduleTask.setState(TaskState.RUNNING);
        scheduleTask.setCorrelationId(CORRELATION_ID);
        scheduleTask.setSourceQueue(QUEUE_NAME);
        scheduleTask.addHistory("One two three");
        scheduleTask.setMaximumAttempts(99L);
        scheduleTask.updateCurrentAttempt();
        scheduleTask.updateCurrentAttempt();

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.markAsComplete(ID);

        verify(dao).findOne(ID);
        assertNull(scheduleTask.getCorrelationId());
        assertNull(scheduleTask.getHistory());
        assertTrue(scheduleTask.isIdle());
        assertEquals(0L, (long) scheduleTask.getCurrentAttempt());
    }

    /**
     * Mark as complete - reset a running task - reset correlation ID - reset
     * history - set as idle
     */
    @Test
    public void thatMarkAsCompleteForRunningTaskWorks_ByCorrelationId() {

        scheduleTask.setState(TaskState.RUNNING);
        scheduleTask.setCorrelationId(CORRELATION_ID);
        scheduleTask.setSourceQueue(QUEUE_NAME);
        scheduleTask.addHistory("One two three");
        scheduleTask.setMaximumAttempts(99L);
        scheduleTask.updateCurrentAttempt();
        scheduleTask.updateCurrentAttempt();

        when(dao.findByCorrelationId(CORRELATION_ID)).thenReturn(scheduleTask);

        runTaskService.markAsComplete(CORRELATION_ID);

        verify(dao).findByCorrelationId(CORRELATION_ID);

        assertNull(scheduleTask.getCorrelationId());
        assertNull(scheduleTask.getHistory());
        assertTrue(scheduleTask.isIdle());
        assertEquals(0L, (long) scheduleTask.getCurrentAttempt());
    }

    /**
     * The task does not run if suspended. - there should be no change to the
     * task
     */
    @Test
    public void thatDoesNotRunIfSuspended() {
        scheduleTask.setCurrentAttempt(0L);
        scheduleTask.setState(TaskState.SUSPENDED);
        scheduleTask.setCorrelationId(CORRELATION_ID);

        when(dao.findOne(ID)).thenReturn(scheduleTask);

        runTaskService.run(ID);

        verify(dao).findOne(ID);
        verifyZeroInteractions(queueService, messageService);

    }

    // when using SpringBootTest, this will override any config
    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public TaskDao dao() {
            return mock(TaskDao.class);
        }

        @Bean
        public MessageService messageService() {
            return mock(MessageService.class);
        }

        @Bean
        public QueueService queueService() {
            return mock(QueueService.class);
        }

        @Bean
        public CorrelationIdService correlationIdService() {
            return mock(CorrelationIdService.class);
        }
    }
}
