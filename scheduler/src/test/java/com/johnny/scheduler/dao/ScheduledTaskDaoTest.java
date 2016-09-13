package com.johnny.scheduler.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.johnny.scheduler.domain.ScheduledTask;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") })
public class ScheduledTaskDaoTest {

    private static final Long ID_WITH_REQUESTS = 1l;
    private static final Long ID_WITH_NO_REQUESTS = 3L;
    private static final String TEST_NAME = "st1";

    @Autowired
    private ScheduledTaskDao dao;

    /*
     * ensure the scripts load and everything is wired up OK ... - redundant
     * once other tests are added
     */
    @Test
    public void thatScriptsLoad() {
        final long count = dao.count();
        assertTrue(count > 0);
    }

    /*
     * Test find by ID, and that associated entities are loaded.
     */
    @Test
    @Transactional
    public void findById() {
        ScheduledTask findOne = dao.findOne(ID_WITH_REQUESTS);
        assertNotNull(findOne);
        assertEquals(3, findOne.getRequests().size());

        findOne = dao.findOne(ID_WITH_NO_REQUESTS);
        assertNotNull(findOne);
        assertThat(findOne.getRequests()).isEmpty();
    }

    @Test
    @Transactional
    public void findByName() {
        final ScheduledTask findByName = dao.findByName(TEST_NAME);
        assertThat(findByName).isNotNull();
        assertThat(findByName.getRequests()).hasSize(3);
    }

    @Test
    @Transactional
    public void findAll() {
        final Iterable<ScheduledTask> findAll = dao.findAll();
        assertEquals(3, findAll.spliterator().getExactSizeIfKnown());

    }
}
