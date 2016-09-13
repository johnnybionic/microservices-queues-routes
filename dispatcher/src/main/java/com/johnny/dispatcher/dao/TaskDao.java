package com.johnny.dispatcher.dao;

import com.johnny.dispatcher.domain.Task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface TaskDao extends CrudRepository<Task, Long> {

    /**
     * Find by correlation ID.
     * 
     * @param id the ID
     * @return the task
     */
    Task findByCorrelationId(String id);
}
