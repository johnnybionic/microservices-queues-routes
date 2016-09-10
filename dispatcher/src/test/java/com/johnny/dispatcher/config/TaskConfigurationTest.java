package com.johnny.dispatcher.config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.RunnableTask;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.TaskMaintenanceServiceDefault;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TaskConfigurationTest {

	private static final Long DELAY_PERIOD = 5000L;

	private static final String CRON_EXPRESSION = "0/5 * 8 * * *";

	@InjectMocks
	private TaskConfiguration configuration;
	
	@Mock
	private TaskMaintenanceServiceDefault taskService;
	
	@Mock
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void thatTasksAreReadAndCreated() {
		
		List<Task> list = new LinkedList<>();
		Task task = new Task();
		task.setDelayPeriod(DELAY_PERIOD);
		
		list.add(task);
		when(taskService.findAll()).thenReturn(list);
		configuration.setUpTasks();
		
		verify(taskService).findAll();
		
	}
	
	@Test
	public void thatOneScheduleOfEachTypeIsCreated() {
		List<Task> list = new LinkedList<>();
		Task task = new Task();
		task.setDelayPeriod(DELAY_PERIOD);
		list.add(task);

		task = new Task();
		task.setCronExpression(CRON_EXPRESSION);
		list.add(task);
		
		when(taskService.findAll()).thenReturn(list);
		configuration.setUpTasks();
		
		verify(taskService).findAll();
		verify(threadPoolTaskScheduler).schedule(any(RunnableTask.class), isA(PeriodicTrigger.class));
		verify(threadPoolTaskScheduler).schedule(any(RunnableTask.class), isA(CronTrigger.class));
	}
}
