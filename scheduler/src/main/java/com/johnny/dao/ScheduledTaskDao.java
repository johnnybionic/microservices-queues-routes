package com.johnny.dao;

import org.springframework.data.repository.CrudRepository;

import com.johnny.domain.ScheduledTask;

public interface ScheduledTaskDao extends CrudRepository<ScheduledTask, Long> {

	ScheduledTask findByName(String name);
}
