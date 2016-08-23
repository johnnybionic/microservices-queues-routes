package com.johnny.external.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Simply a way to get the Environment object.
 *
 * @author johnny
 *
 */
@Configuration
public class EnvironmentConfig implements EnvironmentAware {

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
