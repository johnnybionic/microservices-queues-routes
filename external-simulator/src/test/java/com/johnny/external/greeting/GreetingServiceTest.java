package com.johnny.external.greeting;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GreetingServiceTest {

	private static final String GREETING = "Someone";
	private GreetingService service;
	
	@Before
	public void setUp() {
		service = new GreetingService();
	}
	
	@Test
	public void test() {
		
		Greeting greeting = service.getGreeting(GREETING);
		
		assertNotNull(greeting);
		assertNotNull(greeting.getId());

		assertNotNull(greeting.getContent());
		assertTrue(greeting.getContent().contains(GREETING));

	}

}
