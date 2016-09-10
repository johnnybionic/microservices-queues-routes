package com.johnny.dispatcher.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.TaskMaintenanceService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to retrieve all tasks. Note that the Task repository is
 * {@link RestResource}, so provides REST access already. This controller was
 * written as a test of {@link JsonSerialize} for Date types.
 * 
 * This controller is redundant as the same thing can be achieved with the
 * {@link RestResource}.
 * 
 * @author johnny
 *
 */
@RestController
@RequestMapping("task")
public class TaskController {

    private TaskMaintenanceService taskService;

    @Autowired
    public TaskController(final TaskMaintenanceService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping("all")
    public Collection<Task> getAll() {
        return taskService.findAll();
    }

    @RequestMapping("reset/{id}")
    public void reset(@PathVariable final Long id) {
        taskService.reset(id);
    }

    @RequestMapping("suspend/{id}/{action}")
    public void suspend(@PathVariable final Long id, @PathVariable final Boolean action) {
        taskService.suspend(id, action);
    }
}
