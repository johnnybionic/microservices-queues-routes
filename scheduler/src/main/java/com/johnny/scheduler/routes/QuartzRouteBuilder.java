package com.johnny.scheduler.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Represents a Camel Quartz timer route. 
 * 
 * Multiple routes are created, one for each entry in the database.
 * 
 * I considered using @Prototype for this bean, as Spring would create an instance
 * each time one was requested from the context, but it would also create one unused
 * instance.
 *  
 * @author johnny
 *
 */
@AllArgsConstructor
public class QuartzRouteBuilder extends RouteBuilder {

	private final static String QUARTZ_FORMAT = "quartz2://%s/%s?%s";
	private static final String QUARTZ_DESCRIPTION = "quartz-%s";
	private static final String QUARTZ_MESSAGE = "Running [%s][%s]";
	
	private String groupName;
	private String timerName;
	private String info;
	private String options;
	private Processor processor;
	
	
	@Override
	public void configure() throws Exception {
		
		from(String.format(QUARTZ_FORMAT, groupName, timerName, options))
		.description(String.format(QUARTZ_DESCRIPTION, timerName), info, "en")
		.log(LoggingLevel.INFO, String.format(QUARTZ_MESSAGE, groupName, timerName))
		.process(processor);
	}

}
