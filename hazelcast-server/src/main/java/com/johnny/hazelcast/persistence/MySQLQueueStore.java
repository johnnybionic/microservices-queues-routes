package com.johnny.hazelcast.persistence;

import com.hazelcast.core.QueueStore;
import com.johnny.hazelcast.config.SqlStatements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Persists entries for queues.
 * 
 * An instance is created for each queue (prototype scope), and is given the
 * name of the queue.
 * 
 * http://docs.hazelcast.org/docs/3.5/manual/html/queue-persistence.html
 * 
 * @author johnny
 *
 */
@Repository
@Scope("prototype")
@Slf4j
public class MySQLQueueStore implements QueueStore<String> {

    private final JdbcTemplate jdbcTemplate;

    // better for IN queries - replace use of JdbcTemplate with this
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SqlStatements sqlStatements;

    @Autowired
    public MySQLQueueStore(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            final SqlStatements sqlStatements) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sqlStatements = sqlStatements;
    }

    @Setter
    private String queueName;

    @Override
    public void store(final Long index, final String item) {
        log.info("store queue [{}] index [{}] item [{}]", queueName, index, item);
        final int update = jdbcTemplate.update(sqlStatements.getQueueInsert(), queueName, index, item);
        log.info("Updated: {}", update);
    }

    @Override
    public void delete(final Long index) {
        log.info("deleting {} from [{}]", index, queueName);

        final int update = jdbcTemplate.update(sqlStatements.getQueueDelete(), queueName, index);
        if (update != 1) {
            log.error("Could not delete {}", index);
        }
    }

    @Override
    public void deleteAll(final Collection<Long> keys) {
        log.info("deleting {} from {}: ", keys, queueName);

        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", queueName);
        namedParameters.addValue("keys", keys);

        final int update = namedParameterJdbcTemplate.update(sqlStatements.getQueueDeleteKeys(), namedParameters);
        if (update == 0) {
            log.error("Could not delete {}", keys);
        }
    }

    /**
     * The Hazelcast example returns null if the record isn't found (they're
     * using JDBC directly though).
     */
    @Override
    public String load(final Long index) {
        log.info("loading index {} ... ", index);
        // Spring insists on throwing an exception instead of returning null
        // - doesn't seem to be a problem for queues
        try {
            final String result = jdbcTemplate.queryForObject(sqlStatements.getQueueSelect(), String.class, queueName,
                    index);
            log.info("retrieved id {} for index {}, queue [{}]", result, index, queueName);
            return result;
        }
        catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<Long, String> loadAll(final Collection<Long> keys) {
        log.info("load all {} from {}", keys, queueName);

        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", queueName);
        namedParameters.addValue("keys", keys);

        final Map<Long, String> query = namedParameterJdbcTemplate.query(sqlStatements.getQueueSelectKeys(),
                namedParameters,

                new ResultSetExtractor<Map<Long, String>>() {

                    private final String queueKey = sqlStatements.getQueueKey();
                    private final String queueItem = sqlStatements.getQueueItem();

                    @Override
                    public Map<Long, String> extractData(final ResultSet rs) throws SQLException, DataAccessException {
                        final Map<Long, String> map = new HashMap<>();
                        while (rs.next()) {
                            map.put(rs.getLong(queueKey), rs.getString(queueItem));
                        }
                        return map;
                    }
                });

        return query;
    }

    @Override
    public Set<Long> loadAllKeys() {
        log.info("load all keys");
        final List<Long> ids = jdbcTemplate.queryForList(sqlStatements.getQueueSelectAll(), Long.class, queueName);
        final Set<Long> set = new HashSet<>(ids);
        return set;
    }

    @Override
    public void storeAll(final Map<Long, String> entries) {

        for (final Map.Entry<Long, String> entry : entries.entrySet()) {
            this.store(entry.getKey(), entry.getValue());
        }
    }
}
