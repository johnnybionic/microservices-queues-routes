package com.johnny.dispatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Task;

@Service
@Transactional
public class RunnableTaskService {
	
	@Autowired
	private TaskDao dao;

	@Transactional
	public void addHistory(Long taskId, String message) {
		Task task = dao.findOne(taskId);
		task.addHistory(message);
	}
}
