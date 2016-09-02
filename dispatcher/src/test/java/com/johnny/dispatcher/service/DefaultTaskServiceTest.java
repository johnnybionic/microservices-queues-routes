package com.johnny.dispatcher.service;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Task;

/**
 * Integration test of TaskService. It's just a wrapper round the DAO, and
 * is here so that I follow the standard pattern.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") 
})
public class DefaultTaskServiceTest {

	private static final Long TASK_ID = 1L;

	@Autowired
	private DefaultTaskService taskService;
	
	@Autowired
	private TaskDao taskDao;
	
	@Test
	public void thatAllTasksAreReturned() {
		Collection<Task> findAll = taskService.findAll();
		assertNotNull(findAll);
		assertTrue(findAll.size() > 0);
	}
	
	@Test
	public void thatTaskIsReset() {
		Task findOne = taskDao.findOne(TASK_ID);
		findOne.setWaiting();
		findOne.setCorrelationId("123");
		findOne.updateCurrentAttempt();
		findOne.updateCurrentAttempt();

		Task save = taskDao.save(findOne);
		
		taskService.reset(TASK_ID);

		findOne = taskDao.findOne(TASK_ID);
		assertNull(findOne.getCorrelationId());
		assertEquals(0L, (long)findOne.getCurrentAttempt());
		assertTrue(findOne.isIdle());

	}
}
