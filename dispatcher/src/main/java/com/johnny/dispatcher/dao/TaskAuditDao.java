package com.johnny.dispatcher.dao;

import com.johnny.dispatcher.domain.TaskAudit;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TaskAuditDao extends CrudRepository<TaskAudit, Long> {

    /**
     * Find all audit entries for a particular task.
     * 
     * @param taskid the task's ID
     * @return the entries.
     */
    List<TaskAudit> findByTaskId(Long taskid);

    /**
     * Find all audit entries for a particular correlation ID.
     * 
     * @param correlationid the ID
     * @return the entries.
     */
    List<TaskAudit> findByCorrelationId(String correlationid);
}
