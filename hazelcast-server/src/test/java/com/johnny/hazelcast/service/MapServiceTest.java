package com.johnny.hazelcast.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.johnny.hazelcast.HazelcastServerApplication;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the service that access the map store. As this is simply a lightweight
 * wrapper, this is an integration test and uses a real store (not a mock).
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:insert_map.sql" }),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_map.sql") })
public class MapServiceTest {

    private static final String TEST_ID = "1";
    private static final String TEST_STRING = "Hello";

    /* unit under test. */
    @Autowired
    private MapService<String, String> service;

    @Test
    public void thatAllEntriesAreReturned() {
        Map<String, String> findAll = service.findAll();
        assertNotNull(findAll);
        assertEquals(4, findAll.size());
    }

    @Test
    public void thatOneEntryIsReturned() {
        String findOne = service.findOne(TEST_ID);
        assertNotNull(findOne);
        assertEquals(TEST_STRING, findOne);
    }

}
