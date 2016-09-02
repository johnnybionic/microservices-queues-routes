package com.johnny.hazelcast.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnny.hazelcast.service.MapService;
import com.johnny.hazelcast.service.QueueService;

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

	private MapService<String, String> mapService;
	
	private QueueService<Long, String> queueService;

	@Autowired
	public ServiceController(MapService<String, String> mapService, QueueService<Long, String> queueService) {
		this.mapService = mapService;
		this.queueService = queueService;
	}
	
	@RequestMapping("map")
	public Map<String, String> findAllMap() {
		log.info("Finding all map entries");
		return mapService.findAll();
	}

	@RequestMapping("queue/{queueName:.+}")
	public Map<Long, String> findAllQueue(@PathVariable String queueName) {
		log.info("Finding all queue entries for {}", queueName);
		return queueService.findAll(queueName);
	}

	@RequestMapping("map/{id}")
	public String findOneMap(@PathVariable String id) {
		log.info("Finding map entry {} ", id);
		return mapService.findOne(id);
	}

	@RequestMapping("queue/{queueName:.+}/{id}")
	public String findOneQueue(@PathVariable String queueName, @PathVariable Long id) {
		log.info("Finding queue entry {} for {}", id, queueName);
		return queueService.findOne(queueName, id);
	}
}
