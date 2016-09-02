package com.johnny.dispatcher.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.domain.TaskState;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") 
})

@Transactional
public class TaskDaoTest {

	@Autowired
	private TaskDao dao;
	
	/*
	 * Initial test that scripts load and create on task with one history.
	 */
	@Test
	public void testScriptsLoad() {
		long count = dao.count();
		assertTrue(count > 0);
		Task findOne = dao.findOne(1L);
		assertTrue(findOne.getHistory() != null);
		List<String> history = findOne.getHistory();
		assertTrue(history.size() > 0);
		String string = history.get(0);
		assertNotNull(string);
	}

	@Test
	public void thatStateLoads() {
		Task findOne = dao.findOne(2L);
		assertTrue(findOne.getState().equals(TaskState.IDLE));
		
	}
}
