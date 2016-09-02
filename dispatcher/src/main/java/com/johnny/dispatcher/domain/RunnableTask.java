package com.johnny.dispatcher.domain;

import com.johnny.dispatcher.service.RunTaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Could just make Task runnable ...
 * 
 *  'separation of concerns', and all that :)
 *  
 * @author johnny
 *
 */
@Slf4j
public class RunnableTask implements Runnable {

	private Long taskId;

	private RunTaskService runTaskService;
	
	public RunnableTask(Long taskId, RunTaskService runTaskService) {
		this.taskId = taskId;
		this.runTaskService = runTaskService;
	}

	@Override
	public void run() {
		runTaskService.run(taskId);
	}
}
