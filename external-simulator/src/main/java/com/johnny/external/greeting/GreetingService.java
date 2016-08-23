package com.johnny.external.greeting;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

	static final String TEMPLATE = "Hello, %s!";
	
    private final AtomicLong counter = new AtomicLong();

	public Greeting getGreeting(String content) {
		return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, content));
	}

}
