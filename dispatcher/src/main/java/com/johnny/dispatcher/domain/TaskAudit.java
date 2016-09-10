package com.johnny.dispatcher.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simple logging/auditing task. Instances of this type are not associated
 * with any other entities, although the class does reference a Task by ID.
 * 
 * It is used to create audit entries in the DB.
 * 
 * @author johnny
 *
 */
@Entity
@Data
@NoArgsConstructor
public class TaskAudit {

    public TaskAudit(final Task task, final String message) {
        this.taskId = task.getId();
        this.correlationId = task.getCorrelationId();
        this.message = message;
        this.auditTimestamp = new Date();
    }

    @Id
    @GeneratedValue
    private Long id;

    private Long taskId;
    private String correlationId;
    private Date auditTimestamp;
    private String message;

}
