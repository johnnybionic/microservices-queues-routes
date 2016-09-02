package com.johnny.dispatcher.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.domain.TaskAudit;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") 
})
public class TaskAuditDaoTest {

	private static final Long TASK_ID = 1L;
	private static final String CORRELATION_ID = "123";
	private static final Long NEW_TASK_ID = 42L;
	private static final String NEW_CORRELATION_ID = "new-id";
	private static final String NEW_MESSAGE = "new-message";
	
	@Autowired
	private TaskAuditDao dao;
	
	@Test
	public void thatEntriesFoundByTask() {
		List<TaskAudit> findByTaskId = dao.findByTaskId(TASK_ID);
		assertNotNull(findByTaskId);
		assertTrue(findByTaskId.size() > 0);
	}

	@Test
	public void thatEntriesFoundByCorrelationId() {
		List<TaskAudit> findByCorrelationId = dao.findByCorrelationId(CORRELATION_ID);
		assertNotNull(findByCorrelationId);
		assertTrue(findByCorrelationId.size() > 0);
	}
	
	@Test 
	public void thatEntryCreatedFromTask() {
		Task task = new Task();
		task.setId(NEW_TASK_ID);
		task.setCorrelationId(NEW_CORRELATION_ID);
		dao.save(new TaskAudit(task, NEW_MESSAGE));
		
		List<TaskAudit> findByCorrelationId = dao.findByCorrelationId(NEW_CORRELATION_ID);
		assertNotNull(findByCorrelationId);
		assertTrue(findByCorrelationId.size() == 1);
	}
}
