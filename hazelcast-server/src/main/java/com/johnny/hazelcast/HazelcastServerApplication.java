package com.johnny.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point.
 * 
 * @author johnny
 */
@SpringBootApplication
public final class HazelcastServerApplication {

    private HazelcastServerApplication() {
    }

    /**
     * Main entry point.
     * 
     * @param args command-line
     */
    public static void main(final String[] args) {
        SpringApplication.run(HazelcastServerApplication.class, args);
    }

}
