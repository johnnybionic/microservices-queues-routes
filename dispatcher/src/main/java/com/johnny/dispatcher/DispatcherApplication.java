package com.johnny.dispatcher;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class DispatcherApplication {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * The main entry point.
     * 
     * @param args command line
     */
    public static void main(final String[] args) {
        SpringApplication.run(DispatcherApplication.class, args);

    }

}
