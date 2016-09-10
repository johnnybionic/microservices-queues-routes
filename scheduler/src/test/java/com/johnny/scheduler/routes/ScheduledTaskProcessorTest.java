package com.johnny.scheduler.routes;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.johnny.scheduler.dao.ScheduledTaskDao;
import com.johnny.scheduler.domain.ScheduledTask;
import com.johnny.scheduler.domain.TaskRequest;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScheduledTaskProcessorTest {

    private static final String TEST_TRIGGER_NAME = "trigger1";

    @InjectMocks
    private ScheduledTaskProcessor processor;

    @Mock
    private ScheduledTaskDao dao;

    /* this is supplied by a configuration class */
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Mock
    private IQueue<Object> queue;

    @Before
    public void setUp() {
        // not required
        // MockitoAnnotations.initMocks(this);
    }

    @Autowired
    private CamelContext camelContext;

    @Test
    public void thatRequestsAreSentToQueues() throws Exception {

        final Exchange exchange = new DefaultExchange(camelContext);
        final DefaultMessage defaultMessage = new DefaultMessage();
        defaultMessage.setHeader(ScheduledTaskProcessor.TRIGGER_NAME, TEST_TRIGGER_NAME);

        final List<TaskRequest> requests = new LinkedList<>();
        final ScheduledTask scheduledTask = new ScheduledTask(0L, null, null, null, requests);
        for (long counter = 1; counter < 4; counter++) {
            requests.add(new TaskRequest(counter, scheduledTask, "name" + counter, "document" + counter,
                    "queue." + counter));
        }

        when(dao.findByName(TEST_TRIGGER_NAME)).thenReturn(scheduledTask);
        when(hazelcastInstance.getQueue(anyString())).thenReturn(queue);

        exchange.setIn(defaultMessage);
        processor.process(exchange);

        verify(dao).findByName(TEST_TRIGGER_NAME);
        verify(hazelcastInstance, times(3)).getQueue(anyString());
        verify(queue, times(3)).add(anyString());
    }

}
