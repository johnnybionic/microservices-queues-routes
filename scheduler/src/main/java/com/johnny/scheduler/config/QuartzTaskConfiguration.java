package com.johnny.scheduler.config;

import com.johnny.scheduler.dao.ScheduledTaskDao;
import com.johnny.scheduler.domain.ScheduledTask;
import com.johnny.scheduler.routes.QuartzRouteBuilder;
import com.johnny.scheduler.routes.ScheduledTaskProcessor;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Starts the quartz tasks.
 * 
 * PostConstruct seems to be the easiest way to do this. There are a number of
 * Spring post-initialisation methods but they are rather complex for the task
 * at hand. Plus, PostConstruct is standard Java. Also, it's clear what the
 * intent is.
 * 
 * I used @Configuration even though the class is not really doing what that
 * annotation signifies - but it is creating beans and adding them to a context,
 * it just happens to be Camel.
 * 
 * @author johnny
 *
 */
@Slf4j
@Configuration
// one idea was to have a separate version of this file for demo, with
// tasks/request defined within it
// @Profile("!demo")
public class QuartzTaskConfiguration {

    private static final String SCHEDULE_TASK_GROUP_NAME = "scheduledTaskGroup";
    private final ScheduledTaskDao dao;
    private final CamelContext camelContext;
    private final Processor processor;

    @Autowired
    public QuartzTaskConfiguration(final ScheduledTaskDao dao, final CamelContext camelContext,
            final ScheduledTaskProcessor processor) {
        this.dao = dao;
        this.camelContext = camelContext;
        this.processor = processor;
    }

    /**
     * Create all tasks.
     */
    @PostConstruct
    public void setUp() {

        if (dao.count() == 0) {
            log.error("There must be at least one task");
        }
        else {
            dao.findAll().forEach(task -> {
                addTask(task);
            });
        }
    }

    /**
     * Creates a Camel Quartz route from a {@link ScheduledTask}.
     * 
     * @param task the task to create
     */
    private void addTask(final ScheduledTask task) {
        log.info("Adding task [{}]", task.getName());
        final RouteBuilder builder = new QuartzRouteBuilder(SCHEDULE_TASK_GROUP_NAME, task.getName(),
                task.getComments(), task.getCron(), processor);
        try {
            camelContext.addRoutes(builder);
        }
        catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

}