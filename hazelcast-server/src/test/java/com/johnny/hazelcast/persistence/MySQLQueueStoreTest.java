package com.johnny.hazelcast.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.hazelcast.HazelcastServerApplication;
import com.johnny.hazelcast.config.SqlStatements;
import com.johnny.hazelcast.persistence.MySQLQueueStore;

/**
 * Tests the queue store (the persistence of a queue). Queue stores work with a particular queue (the one
 * given as the queue's name). For the tests, three queues are created in the database, and the store should
 * operate on just one of them.
 * 
 * A queue entry in the database has a name, an ID (the position in the queue) and the data for the entry.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
// the scripts are run before/after each test method. Ensures database is
// exactly as required, and tests won't interfere with each other
// - the standard 'schema.sql' is used to created the table, these scripts populate and
// empty the table
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_queue.sql" }),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_queue.sql") })
public class MySQLQueueStoreTest {


	private static final String TEST_QUEUE_NAME = "queue.1";

	private static final int OTHER_QUEUE__COUNT = 2;
	private static final int QUEUE_1_COUNT = 2;

	@Autowired
	private MySQLQueueStore store;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private SqlStatements sqlStatements;

	private String selectCount;
	private String selectCountQueue1;
	
	@Before
	public void setUp() throws Exception {
		selectCount = String.format("SELECT COUNT(*) AS c FROM %s", sqlStatements.getQueueTable());
		selectCountQueue1 = String.format("SELECT COUNT(*) AS c FROM %s WHERE %s = ?",
				sqlStatements.getQueueTable(), sqlStatements.getQueueName());
		store.setQueueName(TEST_QUEUE_NAME);
	}

	/*
	 * Load one entry from queue 1
	 */
	@Test
	public void testLoad() {
		String item = store.load(1L);
		assertNotNull(item);
		assertEquals(item, "101");
	}

	/*
	 *	Load another queue's ID - should return null. 
	 *	 
	 */
	@Test
	public void thatLoadOfOtherQueueFails () {
		assertNull(store.load(3L));
	}
	
	/*
	 * All entries for one queue, and not for any other.
	 */
	@Test
	public void thatAllEntriesForAQueueAreLoaded() {
		Collection<Long> keys = Arrays.asList(1L, 2L);

		Map<Long, String> loadAll = store.loadAll(keys);
		assertNotNull(loadAll);
		assertThat(loadAll).hasSize(2).containsEntry(1L, "101")
			.containsEntry(2L, "{\"id\":\"102\",\"value\":\"more\"}").doesNotContainEntry(3L, "301");
	}

	/*
	 * Entry is stored.
	 */
	@Test
	public void thatEntryIsStored() {
		store.store(9L, "109");
		String load = store.load(9L);
		assertThat(load).isNotNull().isEqualTo("109");

		assertThat(getTotalCount()).isEqualTo(1 + OTHER_QUEUE__COUNT + QUEUE_1_COUNT);
		assertThat(getQueue1Count()).isEqualTo(1 + QUEUE_1_COUNT);
	}

	/*
	 * All keys for one queue.
	 */
	@Test
	public void thatAllKeysAreLoaded() {
		Set<Long> loadAllKeys = store.loadAllKeys();
		assertThat(loadAllKeys).isNotEmpty().hasSize(QUEUE_1_COUNT);
		assertThat(loadAllKeys).containsExactly(1L, 2L);
	}

	@Test
	public void thatEntryIsDeleted() {
		store.delete(1L);
		assertThat(getTotalCount()).isEqualTo(QUEUE_1_COUNT + OTHER_QUEUE__COUNT - 1);
		assertNull(store.load(1L));
	}

	@Test
	public void thatAllEntriesAreDeleted() {
		Collection<Long> keys = Arrays.asList(1L, 2L);
		store.deleteAll(keys);

		assertThat(getTotalCount()).isEqualTo(OTHER_QUEUE__COUNT);
		assertThat(getQueue1Count()).isEqualTo(0);
	}

	@Test
	public void thatAllEntriesAreStored() {
		Map<Long, String> map = new HashMap<>();
		map.put(88L, "8888");
		map.put(99L, "9999");

		store.storeAll(map);

		assertThat(getTotalCount()).isEqualTo(QUEUE_1_COUNT + OTHER_QUEUE__COUNT + map.size());

		String load = store.load(88L);
		assertThat(load).isEqualTo("8888");

	}

	// --------------------
	private Long getTotalCount() {
		Long rowCount = jdbcTemplate.queryForObject(selectCount, Long.class);
		return rowCount;
	}

	private Long getQueue1Count() {
		Long rowCount = jdbcTemplate.queryForObject(selectCountQueue1, Long.class, TEST_QUEUE_NAME);
		return rowCount;
	}
}
