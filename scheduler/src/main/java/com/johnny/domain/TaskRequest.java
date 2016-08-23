package com.johnny.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single request to fetch a document.
 * 
 * @author johnny
 *
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "scheduledTaskId")
	private ScheduledTask scheduledTask;
	
	private String name;
	
	// can be any format - not restricted to the document's ID
	private String document;
	
	// which Hazelcast queue
	private String queue;
}
