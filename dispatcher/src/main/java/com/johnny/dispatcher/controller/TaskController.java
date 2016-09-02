package com.johnny.dispatcher.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.johnny.dispatcher.domain.Task;
import com.johnny.dispatcher.service.DefaultTaskService;
import com.johnny.dispatcher.service.TaskService;

/**
 * A simple controller to retrieve all tasks. Note that the Task repository
 * is {@link RestResource}, so provides REST access already. This controller
 * was written as a test of {@link JsonSerialize} for Date types.
 * 
 * This controller is redundant as the same thing can be achieved with the {@link RestResource}.
 * 
 * @author johnny
 *
 */
@RestController
@RequestMapping("task")
public class TaskController {

	private TaskService taskService;
	
	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@RequestMapping("all")
	public Collection<Task> getAll() {
		return taskService.findAll();
	}

	@RequestMapping("reset/{id}")
	public void reset(@PathVariable Long id) {
		taskService.reset(id);
	}

}
