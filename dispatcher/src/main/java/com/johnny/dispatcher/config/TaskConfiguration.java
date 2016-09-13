package com.johnny.dispatcher.config;

import com.johnny.dispatcher.domain.RunnableTask;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.RunTaskService;
import com.johnny.dispatcher.service.TaskMaintenanceServiceDefault;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import lombok.extern.slf4j.Slf4j;

/**
 * Creates running tasks (threads) from database entries.
 * 
 * @author johnny
 *
 */
@Slf4j
@Configuration
public class TaskConfiguration {

    private final TaskMaintenanceServiceDefault taskService;
    private final RunTaskService runTaskService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public TaskConfiguration(final TaskMaintenanceServiceDefault taskService,
            final ThreadPoolTaskScheduler threadPoolTaskScheduler, final RunTaskService runTaskService) {
        this.taskService = taskService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.runTaskService = runTaskService;
    }

    /**
     * Creates the tasks. PostContruct seems as good a place as any to do this,
     * and saves messing around with various Spring lifecycle events. Plus, it's
     * standard Java.
     */
    @PostConstruct
    public void setUpTasks() {
        final Iterable<Task> findAll = taskService.findAll();
        if (findAll != null) {
            findAll.forEach(task -> {

                final RunnableTask runnableTask = new RunnableTask(task.getId(), runTaskService);

                if (task.getCronExpression() != null) {
                    log.info("Adding cron schedule for {} : {}", task.getId(), task.getCronExpression());
                    threadPoolTaskScheduler.schedule(runnableTask, new CronTrigger(task.getCronExpression()));
                }
                else if (task.getDelayPeriod() > 0) {
                    log.info("Adding periodic schedule for {} : {}", task.getId(), task.getDelayPeriod());
                    threadPoolTaskScheduler.schedule(runnableTask, new PeriodicTrigger(task.getDelayPeriod()));
                }
                else {
                    log.error("Invalid task {}", task.getId());
                }

            });
        }
    }
}
