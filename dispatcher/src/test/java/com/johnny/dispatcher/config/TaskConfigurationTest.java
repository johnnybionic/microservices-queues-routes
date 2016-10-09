package com.johnny.dispatcher.config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.johnny.dispatcher.domain.RunnableTask;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.TaskMaintenanceServiceDefault;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        // this isn't needed - InjectMocks causes a new instance of the unit to
        // be created, with
        // wiring of the mocks into the constructor. This will causes new mock
        // objects to be created
        // MockitoAnnotations.initMocks(this);
    }

    @Test
    public void thatTasksAreReadAndCreated() {

        final List<Task> list = new LinkedList<>();
        final Task task = new Task();
        task.setDelayPeriod(DELAY_PERIOD);

        list.add(task);
        when(taskService.findAll()).thenReturn(list);
        configuration.setUpTasks();

        verify(taskService).findAll();

    }

    @Test
    public void thatOneScheduleOfEachTypeIsCreated() {
        final List<Task> list = new LinkedList<>();
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

    /**
     * Maybe this should throw an exception instead - capture the current
     * behaviour anyway.
     */
    @Test
    public void thatInvalidTaskIsIgnored() {
        final List<Task> list = new LinkedList<>();
        Task task = new Task();
        list.add(task);

        when(taskService.findAll()).thenReturn(list);
        configuration.setUpTasks();

        verifyZeroInteractions(threadPoolTaskScheduler);
    }
}
