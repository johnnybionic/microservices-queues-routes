package com.johnny.scheduler.routes;

import com.hazelcast.core.HazelcastInstance;
import com.johnny.scheduler.dao.ScheduledTaskDao;
import com.johnny.scheduler.domain.ScheduledTask;
import com.johnny.scheduler.domain.TaskRequest;

import java.util.List;
import java.util.Queue;

import javax.transaction.Transactional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Camel {@link Processor} used in quartz routes, that are used to processor
 * scheduled tasks.
 * 
 * @author johnny
 *
 */
@Slf4j
@Component
public class ScheduledTaskProcessor implements Processor {

    public static final String TRIGGER_NAME = "triggerName";
    public static final String TRIGGER_GROUP = "triggerGroup";

    private final ScheduledTaskDao dao;
    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public ScheduledTaskProcessor(final ScheduledTaskDao dao, final HazelcastInstance hazelcastInstance) {
        this.dao = dao;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    @Transactional
    public void process(final Exchange exchange) throws Exception {

        final String triggerName = exchange.getIn().getHeader(TRIGGER_NAME, String.class);
        log.info("Received trigger for [{}]", triggerName);

        final ScheduledTask findByName = dao.findByName(triggerName);
        if (findByName != null) {
            List<TaskRequest> requests = findByName.getRequests();
            if (requests == null || requests.size() == 0) {
                log.warn("{} has no requests, ignoring", findByName);
            }
            else {
                requests.forEach(request -> {
                    final Queue<String> queue = hazelcastInstance.getQueue(request.getQueue());
                    queue.add(request.getDocument());
                });
            }
        }
    }

}
