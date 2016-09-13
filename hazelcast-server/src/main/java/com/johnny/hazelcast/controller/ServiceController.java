package com.johnny.hazelcast.controller;

import com.johnny.hazelcast.service.MapService;
import com.johnny.hazelcast.service.QueueService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides read-only access to the map and queue storage.
 * 
 * @author johnny
 *
 */
@Slf4j
@RestController
@RequestMapping("/service")
public class ServiceController {

    private final MapService<String, String> mapService;

    private final QueueService<Long, String> queueService;

    @Autowired
    public ServiceController(final MapService<String, String> mapService,
            final QueueService<Long, String> queueService) {
        this.mapService = mapService;
        this.queueService = queueService;
    }

    /**
     * Find all map entries.
     * 
     * @return the entries
     */
    @RequestMapping("map")
    public Map<String, String> findAllMap() {
        log.info("Finding all map entries");
        return mapService.findAll();
    }

    /**
     * Find all entries for a queue. Note that the queue has a format that
     * Spring can confuse as a file name, hence the ".+" qualification.
     * 
     * @param queueName the name of the queue
     * @return a map of the queue's entries
     */
    @RequestMapping("queue/{queueName:.+}")
    public Map<Long, String> findAllQueue(@PathVariable final String queueName) {
        log.info("Finding all queue entries for {}", queueName);
        return queueService.findAll(queueName);
    }

    /**
     * Find a single map entry.
     * 
     * @param id the key
     * @return the value
     */
    @RequestMapping("map/{id}")
    public String findOneMap(@PathVariable final String id) {
        log.info("Finding map entry {} ", id);
        return mapService.findOne(id);
    }

    /**
     * Find a single entry for a queue.
     * 
     * @param queueName the name of the queue
     * @param id the ID of the queue's entry
     * @return the value
     */
    @RequestMapping("queue/{queueName:.+}/{id}")
    public String findOneQueue(@PathVariable final String queueName, @PathVariable final Long id) {
        log.info("Finding queue entry {} for {}", id, queueName);
        return queueService.findOne(queueName, id);
    }
}
