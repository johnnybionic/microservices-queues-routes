package com.johnny.dispatcher.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Task;

/**
 * Redundant. Probably.
 * 
 * @author johnny
 *
 */
@Service
public class DefaultTaskService implements TaskService {

	private TaskDao taskDao;
	
	@Autowired
	public DefaultTaskService(TaskDao taskDao) {
		this.taskDao = taskDao;
	}


	@Override
	public Collection<Task> findAll() {
		
		ArrayList<Task> collection = new ArrayList<Task>();
		taskDao.findAll().forEach(task -> {
			collection.add(task);
		});
		
		return collection;
	}


	@Override
	@Transactional
	public void reset(Long taskId) {
		Task findOne = taskDao.findOne(taskId);
		if (findOne != null) {
			findOne.reset();
		}
	}

}
