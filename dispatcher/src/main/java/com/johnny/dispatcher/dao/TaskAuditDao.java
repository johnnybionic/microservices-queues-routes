package com.johnny.dispatcher.dao;

import org.springframework.data.repository.CrudRepository;

import com.johnny.dispatcher.domain.TaskAudit;
import java.lang.Long;
import java.util.List;
import java.lang.String;

public interface TaskAuditDao extends CrudRepository<TaskAudit, Long> {

	List<TaskAudit> findByTaskId(Long taskid);
	List<TaskAudit> findByCorrelationId(String correlationid);
}
