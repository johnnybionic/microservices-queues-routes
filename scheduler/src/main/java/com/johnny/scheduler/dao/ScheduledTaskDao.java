package com.johnny.scheduler.dao;

import com.johnny.scheduler.domain.ScheduledTask;

import org.springframework.data.repository.CrudRepository;

public interface ScheduledTaskDao extends CrudRepository<ScheduledTask, Long> {

    /**
     * Additional find by name.
     * 
     * @param name the name
     * @return the {@link ScheduledTask}
     */
    ScheduledTask findByName(String name);
}
