package com.johnny.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class SchedulerApplication {

    /**
     * Main entry point.
     * 
     * @param args command-line
     */
    public static void main(final String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

    private SchedulerApplication() {
    }

}
