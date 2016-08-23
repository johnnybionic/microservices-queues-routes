package com.johnny.dispatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class DispatcherApplication {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    
    //@Value("${}")
    
	public static void main(String[] args) {
		SpringApplication.run(DispatcherApplication.class, args);
		
	}
	
	@PostConstruct
	public void setUp() {
		//TaskScheduler scheduler = new ThreadPoolTaskScheduler();
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Running!");
			}
		};
		
		threadPoolTaskScheduler.scheduleAtFixedRate(task , 5000);
		
	}
}
