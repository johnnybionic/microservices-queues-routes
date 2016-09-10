package com.johnny.dispatcher.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.johnny.dispatcher.domain.Task;

@RestResource
public interface TaskDao extends CrudRepository<Task, Long> {

	Task findByCorrelationId(String id);
}
