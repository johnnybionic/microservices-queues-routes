package com.johnny.dispatcher.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.johnny.dispatcher.service.HazelcastCorrelationIdService.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HazelcastCorrelationIdServiceTest {

	private static final Object CORRELATION_ID = "1000";
	private static final Object NEXT_CORRELATION_ID = "1001";

	@InjectMocks
	private HazelcastCorrelationIdService service;
	
	// it's like this as it gets injected via constructor - see HazelcastConfig.java
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Mock
	private IMap<Object, Object> map;

	@Before
	public void setUp() throws Exception {
		reset(hazelcastInstance);
	}

	@Test
	public void testThatIdIsRetrieved() {
		when(hazelcastInstance.getMap(CORRELATION_ID_MAP_NAME)).thenReturn(map);
		when(map.get(CORRELATION_ID_MAP_KEY)).thenReturn(CORRELATION_ID);
		
		String correlationId = service.getCorrelationId();
		
		verify(hazelcastInstance).getMap(CORRELATION_ID_MAP_NAME);
		verify(map).get(CORRELATION_ID_MAP_KEY);
		verify(map).put(CORRELATION_ID_MAP_KEY, NEXT_CORRELATION_ID);
		
		assertNotNull(correlationId);
		assertEquals(CORRELATION_ID, correlationId);
	}

	@Test
	public void thatIdInitialisedIfNotFound() {
		when(hazelcastInstance.getMap(CORRELATION_ID_MAP_NAME)).thenReturn(map);
		when(map.get(CORRELATION_ID_MAP_KEY)).thenReturn(null);
		
		String correlationId = service.getCorrelationId();
		
		verify(hazelcastInstance).getMap(CORRELATION_ID_MAP_NAME);
		verify(map).get(CORRELATION_ID_MAP_KEY);
		String newValue = String.valueOf((Long.valueOf(INITIAL_CORRELATION_ID) + 1));
		verify(map).put(CORRELATION_ID_MAP_KEY, newValue);
		
		assertNotNull(correlationId);
		assertEquals(INITIAL_CORRELATION_ID, correlationId);
		
	}
}
