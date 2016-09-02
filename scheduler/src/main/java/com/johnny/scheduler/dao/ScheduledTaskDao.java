package com.johnny.scheduler.dao;

import org.springframework.data.repository.CrudRepository;

import com.johnny.scheduler.domain.ScheduledTask;

public interface ScheduledTaskDao extends CrudRepository<ScheduledTask, Long> {

	ScheduledTask findByName(String name);
}
