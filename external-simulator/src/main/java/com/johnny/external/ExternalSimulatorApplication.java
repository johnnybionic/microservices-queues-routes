package com.johnny.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms 
// this is not required, as SpringBootApplication includes it - but only for the same package
//@ComponentScan(basePackages="com.johnny.external")
public class ExternalSimulatorApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ExternalSimulatorApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(ExternalSimulatorApplication.class, args);
		
	}
}
