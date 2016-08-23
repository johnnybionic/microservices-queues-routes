package com.johnny.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.HazelcastServerApplication;

/**
 * Integration test for the service controller. This
 * provides read-only access to both maps and queues.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_queue.sql", "classpath:insert_map.sql" }),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:delete_queue.sql", "classpath:delete_map.sql"}) })
public class ServiceControllerTest {

	private static final Long TEST_ID = 1L;
	private static final String TEST_STRING = "Hello";
	private static final String QUEUE_NAME = "queue.1";

	/* unit under test. */
	@Autowired
	private ServiceController controller;
	
	@Test
	public void thatAllMapEntriesAreReturned() {
		Map<Long, String> findAll = controller.findAllMap();
		assertNotNull(findAll);
		assertEquals(4, findAll.size());
	}

	@Test
	public void thatAllQueueEntriesAreReturned() {
		Map<Long, String> findAll = controller.findAllQueue(QUEUE_NAME);
		assertNotNull(findAll);
		assertEquals(2, findAll.size());
	}

	@Test
	public void thatOneMapEntryIsReturned() {
		String findOne = controller.findOneMap(TEST_ID);
		assertNotNull(findOne);
		assertEquals(TEST_STRING, findOne);

	}

	@Test
	public void thatOneQueueIsReturned() {
		String findOne = controller.findOneQueue(QUEUE_NAME, TEST_ID);
		assertNotNull(findOne);
		assertEquals("101", findOne);

	}

}
