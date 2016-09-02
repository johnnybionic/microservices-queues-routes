package com.johnny.hazelcast.persistence;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.hazelcast.HazelcastServerApplication;
import com.johnny.hazelcast.persistence.MySQLMapStore;

/**
 * Tests the store of a Long:String map.
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
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_map.sql" }),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_map.sql") })
public class MySQLMapStoreTest {

	private static final String[] KEYS = new String[] {"1", "Tw0", "3hre£", "4our"};
	private static final String TEST_KEY = "1";
	private static final String TEST_VALUE = "Hello";
	private static final String NEW_KEY = "9.9";
	private static final String NEW_VALUE = "ninety-nine";
	private static final String NEW_VALUE_UPSERT = "one-hundred-and-eighty!!";

	@Autowired
	private MySQLMapStore store;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void thatLoadSinglePairWorks() {
		String value = store.load(TEST_KEY);
		assertEquals(TEST_VALUE, value);
	}

	@Test
	public void thatLoadAllPairsWorks() {
		
		String[] keyArray = KEYS;
		Collection<String> keys = Arrays.asList(keyArray);
		Map<String, String> loadAll = store.loadAll(keys);
		assertEquals(keyArray.length, loadAll.size());
	}
	
	@Test
	public void thatLoadAllKeysWorks() {

		String[] keyArray = KEYS;
		Collection<String> keys = new LinkedList<>(Arrays.asList(keyArray));
		
		Iterable<String> allKeys = store.loadAllKeys();
		allKeys.forEach(key -> {
			assertTrue(keys.contains(key));
			keys.remove(key);
		});

		assertEquals(0, keys.size());
	}
	
	@Test
	public void thatSinglePairIsStored () {

		Long count = getTotalCount();
		
		store.store(NEW_KEY, NEW_VALUE);
		
		assertEquals((Long)(count + 1), getTotalCount());
		assertNotNull(store.load(NEW_KEY));
	}
	
	@Test
	public void thatDuplicateKeyCausesUpsert() {
		Long count = getTotalCount();
		
		store.store(NEW_KEY, NEW_VALUE);
		store.store(NEW_KEY, NEW_VALUE_UPSERT);
		
		assertEquals((Long)(count + 1), getTotalCount());
		String load = store.load(NEW_KEY);
		assertNotNull(load);
		assertEquals(NEW_VALUE_UPSERT, load);

	}
	
	@Test
	public void thatCollectionOfPairsIsStored() {
		Map<String, String> pairs = new HashMap<>();
		Arrays.asList(KEYS).forEach(key -> {
			pairs.put(key + 100, key);
		});
		
		Long count = getTotalCount();
		
		store.storeAll(pairs);
		
		assertEquals((Long)(count + KEYS.length), getTotalCount());

	}
	
	@Test
	public void thatSinglePairIsDeleted () {
		Long count = getTotalCount();

		store.delete(TEST_KEY);
		assertEquals((Long)(count - 1), getTotalCount());
		
	}
	
	@Test
	public void thatCollectionOfPairsIsDeleted () {
		store.deleteAll(Arrays.asList(KEYS));
		assertEquals((Long)0L, getTotalCount());
	}
	
	private Long getTotalCount() {
		Long rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) AS c FROM map_string_to_string", Long.class);
		return rowCount;
	}

}
