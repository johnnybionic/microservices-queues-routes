package com.johnny.external.greeting;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GreetingControllerTest {

	private static final String HELLO = null;
	private GreetingController controller;
	
	GreetingService mockGreetingService;
	
	@Before
	public void setUp() {
		controller = new GreetingController();
		mockGreetingService = mock(GreetingService.class);
		controller.setService(mockGreetingService);
	}
	
	@Test
	public void test() {
		
		Greeting greeting = new Greeting(1, HELLO);
		when(mockGreetingService.getGreeting(HELLO)).thenReturn(greeting);
		Greeting response = controller.greeting(HELLO);
		assertEquals(response, greeting);
	}

}
