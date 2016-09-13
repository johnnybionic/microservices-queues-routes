package com.johnny.scheduler.configuration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.johnny.scheduler.config.QuartzTaskConfiguration;
import com.johnny.scheduler.dao.ScheduledTaskDao;
import com.johnny.scheduler.domain.ScheduledTask;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuartzTaskConfigurationTest {

    private static final String NAME = "name";
    private static final String CRON = "cron";
    private static final String COMMENTS = "comments";

    @InjectMocks
    private QuartzTaskConfiguration configuration;

    @Mock
    private CamelContext camelContext;

    @Mock
    private ScheduledTaskDao dao;

    @Before
    public void startUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void thatRoutesAreAddedToCamel() throws Exception {

        final Collection<ScheduledTask> list = new LinkedList<>();
        final long numberOfRoutes = 3;
        for (long counter = 0; counter < numberOfRoutes; counter++) {
            list.add(getScheduleTask(counter + 1));
        }

        when(dao.count()).thenReturn(numberOfRoutes);
        when(dao.findAll()).thenReturn(list);

        configuration.setUp();

        verify(dao).findAll();
        verify(camelContext, times((int) numberOfRoutes)).addRoutes(any(RouteBuilder.class));
    }

    private ScheduledTask getScheduleTask(final Long id) {
        return new ScheduledTask(id, NAME + " " + id, CRON + " " + id, COMMENTS + " " + id, null);
    }

}
