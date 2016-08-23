package com.johnny.external.greeting;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GreetingTest {

	private static final long ID = 1;
	private static final String CONTENT = "Greetings!";
	
	private Greeting greeting;

	@Before
	public void setUp() {
		greeting = new Greeting(ID, CONTENT);
	}
	
	@Test
	public void testGetId() {
		assertEquals(ID, greeting.getId());
	}

	@Test
	public void testGetContent() {
		assertEquals(CONTENT, greeting.getContent());
	}


}
