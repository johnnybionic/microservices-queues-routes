package com.johnny.hazelcast.persistence.old;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hazelcast.core.MapStore;
import com.johnny.hazelcast.config.SqlStatements;

import lombok.extern.slf4j.Slf4j;

/**
 * Obsolete: this stored Long:String, it's String:String now
 * 
 * Store for maps.
 * 
 * Based on: http://docs.hazelcast.org/docs/3.5/manual/html/map-persistence.htm
 * 
 * @author johnny
 *
 */
//@Repository
//@Scope("prototype")
@Slf4j
public class MySQLMapStore implements MapStore<Long, String> {

	private final JdbcTemplate jdbcTemplate;
	private final SqlStatements sqlStatements;

	@Autowired
	public MySQLMapStore(JdbcTemplate jdbcTemplate, SqlStatements sqlStatements) {
		this.jdbcTemplate = jdbcTemplate;
		this.sqlStatements = sqlStatements;
	}

	/**
	 * The Hazelcast example returns null if the record isn't found (they're
	 * using JDBC directly though).
	 * 
	 * The method is called every time an entry is added.
	 */

	@Override
	public String load(Long key) {
		log.info("Loading key [{}]", key);
		// Spring insists on throwing an exception instead of returning null
		// - maybe there's a better way to do this
		try {
			String value = jdbcTemplate.queryForObject(sqlStatements.getMapSelectSingle(), String.class, key);
			log.info("Retrieved value [{}] for key [{}]", value, key);
			return value;
		} 
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Map<Long, String> loadAll(Collection<Long> keys) {
		// Not very efficient, but based on a Hazelcast example :)
		log.info("Loading all for {}", keys);
		Map<Long, String> map = new HashMap<>();
		keys.forEach(key -> {
			map.put(key, load(key));
		});
		return map;
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		log.info("Loading all keys");
		List<Long> list = jdbcTemplate.queryForList(sqlStatements.getMapSelectAll(), Long.class);
		return list;
	}

	// this INSERT is an UPSERT - note use of H2 database in unit tests as it can handle the non-standard MySQL extension
	@Override
	public void store(Long key, String value) {
		log.info("Storing key [{}] value [{}]", key, value);
		int update = jdbcTemplate.update(sqlStatements.getMapInsert(), key, value, value);
		log.info("Store resulted in {}", update);
	}

	@Override
	public void storeAll(Map<Long, String> map) {
		// again, not efficient ...
		log.info("Storing all ...");
		map.keySet().forEach(key -> {
			store(key, map.get(key));
		});
	}

	@Override
	public void delete(Long key) {
		log.info("Deleting key [{}]", key);
		jdbcTemplate.update(sqlStatements.getMapDelete(), key);
	}

	@Override
	public void deleteAll(Collection<Long> keys) {
		// and again ... :)
		log.info("Deleting all keys: {}", keys);
		keys.forEach(key -> {
			delete(key);
		});
	}

}
