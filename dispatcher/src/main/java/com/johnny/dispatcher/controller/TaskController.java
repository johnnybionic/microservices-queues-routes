package com.johnny.dispatcher.controller;

import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.TaskMaintenanceService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to retrieve all tasks.
 * 
 * Also provides endpoints to reset and suspend/reinstate a task.
 * 
 * @author johnny
 *
 */
@RestController
@RequestMapping("task")
public class TaskController {

    private final TaskMaintenanceService taskService;

    @Autowired
    public TaskController(final TaskMaintenanceService taskService) {
        this.taskService = taskService;
    }

    /**
     * Find all.
     * 
     * @return the tasks
     */
    @RequestMapping("all")
    public Collection<Task> getAll() {
        return taskService.findAll();
    }

    /**
     * Reset a task, e.g. if it's running and blocked.
     * 
     * @param id the ID of the task
     */
    @RequestMapping("reset/{id}")
    public void reset(@PathVariable final Long id) {
        taskService.reset(id);
    }

    /**
     * Suspend a task, which prevents it from running.
     * 
     * @param id the ID of the task
     * @param action true to suspend, false to reinstate.
     */
    @RequestMapping("suspend/{id}/{action}")
    public void suspend(@PathVariable final Long id, @PathVariable final Boolean action) {
        taskService.suspend(id, action);
    }
}
