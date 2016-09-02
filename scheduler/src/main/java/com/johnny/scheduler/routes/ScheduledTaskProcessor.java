package com.johnny.scheduler.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.johnny.scheduler.dao.ScheduledTaskDao;
import com.johnny.scheduler.domain.ScheduledTask;

import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Queue;

import javax.transaction.Transactional;

/**
 * Camel {@link Processor} used in quartz routes, that are used to
 * processor scheduled tasks. 
 * 
 * @author johnny
 *
 */
@Slf4j
@Component
public class ScheduledTaskProcessor implements Processor {

	public static final String TRIGGER_NAME = "triggerName";
	public static final String TRIGGER_GROUP = "triggerGroup";

	private ScheduledTaskDao dao;
	private HazelcastInstance hazelcastInstance;


	@Autowired
	public ScheduledTaskProcessor (ScheduledTaskDao dao, HazelcastInstance hazelcastInstance) {
		this.dao = dao;
		this.hazelcastInstance = hazelcastInstance;
	}
	
	@Override
	@Transactional
	public void process(Exchange exchange) throws Exception {

		String triggerName = exchange.getIn().getHeader(TRIGGER_NAME, String.class);
		log.info("Received trigger for [{}]", triggerName);
		
		ScheduledTask findByName = dao.findByName(triggerName);
		// TODO: won't this throw an exception before getting here?
		if (findByName != null) {
			if (findByName.getRequests() == null || findByName.getRequests().size() == 0) {
				log.warn("{} has no requests, ignoring", findByName);
			}
			else {
				findByName.getRequests().forEach(request -> {
					Queue<String> queue = hazelcastInstance.getQueue(request.getQueue());
					queue.add(request.getDocument());
				});
			}
		}
	}

}
