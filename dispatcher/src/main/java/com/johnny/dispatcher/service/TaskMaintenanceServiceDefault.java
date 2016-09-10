package com.johnny.dispatcher.service;

import com.johnny.dispatcher.dao.TaskDao;
import com.johnny.dispatcher.domain.Task;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Redundant. Probably.
 * 
 * @author johnny
 *
 */
@Service
public class TaskMaintenanceServiceDefault implements TaskMaintenanceService {

    private final TaskDao taskDao;

    @Autowired
    public TaskMaintenanceServiceDefault(final TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Collection<Task> findAll() {

        final ArrayList<Task> collection = new ArrayList<Task>();
        taskDao.findAll().forEach(task -> {
            collection.add(task);
        });

        return collection;
    }

    @Override
    @Transactional
    public void reset(final Long taskId) {
        final Task findOne = taskDao.findOne(taskId);
        if (findOne != null) {
            findOne.reset();
        }
    }

    @Override
    @Transactional
    public void suspend(final Long id, final Boolean action) {
        final Task findOne = taskDao.findOne(id);
        findOne.setSuspended(action);
    }

}
