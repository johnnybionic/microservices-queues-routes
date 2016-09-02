package com.johnny.dispatcher.domain;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.johnny.dispatcher.service.RunTaskService;

/**
 * This class is simple an implementation of {@link Runnable} that is
 * used for timed runs of {@link RunTaskService}. 
 * 
 * One notable reason for having an injected, Spring-managed bean doing the 
 * work is that it can be proxied and @Transactional.
 * 
 * @author johnny
 *
 */
public class RunnableTaskTest {

	private static final String NAME = "Task 1";
	private static final Long ID = 1L;

	private RunnableTask task;

	private Task scheduleTask;

	@Mock
	private RunTaskService runTaskService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		scheduleTask = new Task();
		scheduleTask.setName(NAME);
		scheduleTask.setId(ID);
		
		task = new RunnableTask(scheduleTask.getId(), runTaskService);
	}

	@Test
	public void thatServiceIsInvoked() {
		
		task.run();
		verify(runTaskService).run(ID);
	}
}
