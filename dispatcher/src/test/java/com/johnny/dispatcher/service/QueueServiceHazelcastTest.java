package com.johnny.dispatcher.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.johnny.dispatcher.domain.Document;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QueueServiceHazelcastTest {

    private static final String QUEUE_NAME = "queue.1";

    private static final Long ID = 42L;

    // the normal wiring will inject the mock Hazelcast instance below
    // - plus the ObjectMapper
    @Autowired
    private QueueServiceHazelcast service;

    // it's like this as it gets injected via constructor - see
    // HazelcastConfig.java
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Mock
    private IQueue<Object> queue;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        // MockitoAnnotations.initMocks(this);
        reset(hazelcastInstance);
    }

    /**
     * This test also covers a queue not existing, in which case it will be
     * created and be empty. Hazelcast has getInstances() which could be used to
     * check if the queue is there, but it could be expensive for every call,
     * and it an be assumed that the app is set up correctly. OK, maybe not :)
     * Another solution is to have counter for how many times a queue can return
     * empty before an alert is raised.
     */
    @Test
    public void thatNothingIsReturnedIfQueueEmpty() {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(0);

        final Document readQueue = service.readQueue(QUEUE_NAME);

        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        verify(queue, never()).poll();
        assertNull(readQueue);
    }

    @Test
    public void thatDocumentReturnedIfWaitingOnQueue_FullJson() throws JsonProcessingException {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(1);
        final Document document = new Document(ID, null, null);

        final String json = objectMapper.writeValueAsString(document);
        when(queue.poll()).thenReturn(json);

        final Document readQueue = service.readQueue(QUEUE_NAME);

        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        verify(queue).poll();
        assertNotNull(readQueue);
        assertTrue(document.equals(readQueue));

    }

    @Test
    public void thatDocumentReturnedIfWaitingOnQueue_PartialJson() throws JsonProcessingException {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(1);
        final Document document = new Document(ID, null, null);

        final String json = "{\"id\":42}";
        when(queue.poll()).thenReturn(json);

        final Document readQueue = service.readQueue(QUEUE_NAME);

        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        verify(queue).poll();
        assertNotNull(readQueue);
        assertTrue(document.equals(readQueue));

    }

    @Test
    public void thatNothingIsReturnedForBadDocument() {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(27);
        when(queue.poll()).thenReturn("Whoops!");

        final Document readQueue = service.readQueue(QUEUE_NAME);
        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        assertNull(readQueue);

    }

    @Test
    public void thatBadTypeDoesNotCauseAProblem() {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(27);
        when(queue.poll()).thenReturn(new Integer(1));

        final Document readQueue = service.readQueue(QUEUE_NAME);
        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        assertNull(readQueue);
    }

    @Test
    public void thatHasEntryReturnsTrue() {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(27);

        final boolean hasEntry = service.hasEntry(QUEUE_NAME);
        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        assertTrue(hasEntry);
    }

    @Test
    public void thatHasEntryReturnsFalse() {
        when(hazelcastInstance.getQueue(QUEUE_NAME)).thenReturn(queue);
        when(queue.size()).thenReturn(0);

        final boolean hasEntry = service.hasEntry(QUEUE_NAME);
        verify(hazelcastInstance).getQueue(QUEUE_NAME);
        verify(queue).size();
        assertFalse(hasEntry);
    }
}
