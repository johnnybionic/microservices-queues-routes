package com.johnny.hazelcast.service;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.hazelcast.HazelcastServerApplication;
import com.johnny.hazelcast.service.QueueService;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_queue.sql" }),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_queue.sql") })
public class QueueServiceTest {

	private static final String QUEUE_NAME = "queue.1";

	private static final Long TEST_ID = 1L;
	
	/* unit under test. */
	@Autowired
	private QueueService<Long, String> service;
	
	@Test
	public void thatAllEntriesAreReturned() {
		Map<Long, String> findAll = service.findAll(QUEUE_NAME);
		assertNotNull(findAll);
		assertEquals(2, findAll.size());
	}

	@Test
	public void thatOneEntryIsReturned() {
		String findOne = service.findOne(QUEUE_NAME, TEST_ID);
		assertNotNull(findOne);
		assertEquals("101", findOne);
		
	}
}
