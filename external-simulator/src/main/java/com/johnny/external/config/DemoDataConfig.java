package com.johnny.external.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

/**
 * Initialises the database when running in demo mode.
 * 
 * As the module uses native JDBC, it's not possible to use the handy features of
 * Hibernate etc. to create and populate the database, so it's all done 'by hand'.
 * Luckily the DB is very simple. Also, as it's not read until requests are received,
 * the DB can be initialised at any point during startup.
 *  
 * @author johnny
 *
 */
@Slf4j
@Configuration
@Profile("demo")
public class DemoDataConfig {

	private static final String TABLE_SQL = "CREATE TABLE document (id INT PRIMARY KEY, title VARCHAR(255), content VARCHAR(255));";
	private static final String ROW_1_SQL = "INSERT INTO document VALUES (1, 'Document 1', 'This is the content for document 1');";
	private static final String ROW_2_SQL = "INSERT INTO document VALUES (2, 'Document 2', 'This is the content for document 2');";

	@Autowired
	private DataSource dataSource;
	
	// taken from an example in "Pro Spring Boot"
	// - not sure if it's any better than @PostConstruct in this case though
	@Bean
	public InitializingBean getData() {
		return () -> {
			createDB();
			initData();
		};
	}

	private void initData() {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			
			int counter = statement.executeUpdate(ROW_1_SQL);
			counter += statement.executeUpdate(ROW_2_SQL);
			log.info("Created {} rows", counter);
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		
	}

	private void createDB() {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			
			boolean execute = statement.execute(TABLE_SQL);
		}
		catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		
	}
}
