package com.johnny.dispatcher.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.RunnableTask;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.CorrelationIdService;
import com.johnny.dispatcher.service.MessageService;
import com.johnny.dispatcher.service.QueueService;
import com.johnny.dispatcher.service.RunTaskService;
import com.johnny.dispatcher.service.RunnableTaskService;
import com.johnny.dispatcher.service.DefaultTaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration

public class TaskConfiguration {

	private DefaultTaskService taskService;
	private RunTaskService runTaskService;
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	@Autowired
	public TaskConfiguration(DefaultTaskService taskService, ThreadPoolTaskScheduler threadPoolTaskScheduler, RunTaskService runTaskService) {
		this.taskService = taskService;
		this.threadPoolTaskScheduler = threadPoolTaskScheduler;
		this.runTaskService = runTaskService;
	}

	@PostConstruct
	public void setUpTasks() {
		Iterable<Task> findAll = taskService.findAll();
		if (findAll != null) {
			findAll.forEach(task -> {
				
				RunnableTask runnableTask = new RunnableTask(task.getId(), runTaskService);
				
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
