package com.johnny.dispatcher.dao;

import com.johnny.dispatcher.domain.TaskAudit;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TaskAuditDao extends CrudRepository<TaskAudit, Long> {

    List<TaskAudit> findByTaskId(Long taskid);

    List<TaskAudit> findByCorrelationId(String correlationid);
}
