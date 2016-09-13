package com.johnny.dispatcher.domain;

import com.johnny.dispatcher.service.RunTaskService;

/**
 * Could just make Task runnable ...
 * 
 * 'separation of concerns', and all that :)
 * 
 * @author johnny
 *
 */
public class RunnableTask implements Runnable {

    private final Long taskId;

    private final RunTaskService runTaskService;

    public RunnableTask(final Long taskId, final RunTaskService runTaskService) {
        this.taskId = taskId;
        this.runTaskService = runTaskService;
    }

    @Override
    public void run() {
        runTaskService.run(taskId);
    }
}
