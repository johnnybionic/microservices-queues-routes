package com.johnny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a scheduled task, which fires one or more requests. 

 * @author johnny
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledTask {

	@Id
	private Long id;
	
	private String name;
	
	private String cron;
	
	private String comments;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduledTask")
	private List<TaskRequest> requests;
}
