package com.johnny.dispatcher.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Task;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test of TaskService. It's just a wrapper round the DAO, and is
 * here so that I follow the standard pattern.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") })
public class TaskMaintenanceServiceDefaultTest {

    private static final Long TASK_ID = 1L;

    @Autowired
    private TaskMaintenanceServiceDefault taskService;

    @Autowired
    private TaskDao taskDao;

    @Test
    public void thatAllTasksAreReturned() {
        final Collection<Task> findAll = taskService.findAll();
        assertNotNull(findAll);
        assertTrue(findAll.size() > 0);
    }

    @Test
    public void thatTaskIsReset() {
        Task findOne = taskDao.findOne(TASK_ID);
        findOne.setRunning();
        findOne.setCorrelationId("123");
        findOne.updateCurrentAttempt();
        findOne.updateCurrentAttempt();

        taskDao.save(findOne);

        taskService.reset(TASK_ID);

        findOne = taskDao.findOne(TASK_ID);
        assertNull(findOne.getCorrelationId());
        assertEquals(0L, (long) findOne.getCurrentAttempt());
        assertTrue(findOne.isIdle());

    }

    @Test
    public void thatTaskIsSuspended() {
        Task findOne = taskDao.findOne(TASK_ID);
        findOne.setSuspended(false);
        taskDao.save(findOne);

        taskService.suspend(TASK_ID, true);

        findOne = taskDao.findOne(TASK_ID);
        assertTrue(findOne.isSuspended());
    }

    @Test
    public void thatTaskIsReinstated() {
        Task findOne = taskDao.findOne(TASK_ID);
        findOne.setSuspended(true);
        taskDao.save(findOne);

        taskService.suspend(TASK_ID, false);

        findOne = taskDao.findOne(TASK_ID);
        assertFalse(findOne.isSuspended());
    }

}
