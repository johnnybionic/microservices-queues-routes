package com.johnny;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.johnny.persistence.MySQLQueueStore;

/**
 * Start Hazelcast and populate with test data. Ensure data shared between two clients. 
 * 
 * @author johnny
 *
 */

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_queue.sql" }),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_queue.sql") })
public class HazelcastServerTest {

	private static final String QUEUE_NAME = "queue.1";

	@Value("${hazelcast.group}")
	private String groupName;

	@Value("${hazelcast.password}")
	private String password;

	@Value("${hazelcast.port}")
	private int port;

	@Autowired
	private MySQLQueueStore store;

	@Before
	public void setUp() throws Exception {
		store.setQueueName(QUEUE_NAME);
	}

	@Test
	public void testInstanceCreated() {
		HazelcastInstance client1 = getHazelcastInstance();

		// read the test data
		Queue<String> queueOne = client1.getQueue(QUEUE_NAME);
		String poll = queueOne.poll();
		assertTrue(poll.equals("101"));

		poll = queueOne.poll();
		assertTrue(poll.equals("{\"id\":\"102\",\"value\":\"more\"}"));
		
		// queue now empty
		assertTrue(queueOne.size() == 0);
		
		// ensure write by one client is read by another
		queueOne.offer("777");

		HazelcastInstance client2 = getHazelcastInstance();
		Queue<String> queueTwo = client2.getQueue(QUEUE_NAME);
		poll = queueTwo.poll();
		assertTrue(poll.equals("777"));
	}

	private HazelcastInstance getHazelcastInstance() {
		ClientConfig config = new ClientConfig();
		config.getGroupConfig().setName(groupName).setPassword(password);
		config.getNetworkConfig().setAddresses(Arrays.asList("localhost:" + port)).setConnectionAttemptLimit(5)
				.setConnectionAttemptPeriod(1000);

		return HazelcastClient.newHazelcastClient(config);
	}

}
