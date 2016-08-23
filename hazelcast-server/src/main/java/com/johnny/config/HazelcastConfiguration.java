package com.johnny.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapStoreFactory;
import com.hazelcast.core.QueueStoreFactory;
import com.johnny.persistence.MySQLQueueStoreFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * https://opencredo.com/spring-booting-hazelcast/
 * 
 * http://docs.hazelcast.org/docs/3.5/manual/html/usingwildcard.html
 * 
 * Documentation for queue persistence is a bit lacking, but
 * it can be worked out by looking at map persistence:
 * 
 * http://docs.hazelcast.org/docs/3.5/manual/html/map-persistence.html
 * 
 * @author johnny
 *
 */
@Configuration
@Slf4j
public class HazelcastConfiguration {

	@Value("${hazelcast.group:hazelcast-group}")
	private String groupName;

	@Value("${hazelcast.password:hazelcast-password}")
	private String password;

	@Value("${hazelcast.port:5701}")
	private int clientPort;

	/**
	 * Configure the instance. Spring starts the instance automatically if
	 * a {@link Config} bean is instantiated.
	 * 
	 * http://docs.hazelcast.org/docs/3.3/manual/html/config.html
	 * 
	 * @param queueStoreFactory factory that provides QueueStore
	 * @param mapStoreFactory factory that provides MapStore
	 * @return
	 */
	@Bean
	@Autowired
	public Config getHazelcastConfig(QueueStoreFactory<String> queueStoreFactory,
			MapStoreFactory<Long, String> mapStoreFactory) {

		Config config = new Config();
		config.getGroupConfig().setName(groupName).setPassword(password);
		config.getNetworkConfig().setPort(clientPort);
		
		// configure the map storage - it's the same for every map, hence wildcard
		MapConfig mapConfig = config.getMapConfig("*");
		mapConfig.setBackupCount(1);
		MapStoreConfig mapStoreConfig = new MapStoreConfig()
				.setEnabled(true)
				.setProperty("binary", "false")
				.setFactoryImplementation(mapStoreFactory)
				;
		mapConfig.setMapStoreConfig(mapStoreConfig);

		// configure the queue storage
		// configure each queue starting with "queue"
		QueueConfig queueConfig = config.getQueueConfig("queue.*");
		queueConfig.setBackupCount(1);

		// configure the queue's store
		QueueStoreConfig queueStoreConfig = new QueueStoreConfig()
				.setEnabled(true)
				.setProperty("binary", "false")
				.setFactoryImplementation(queueStoreFactory)
				;
		
		queueConfig.setQueueStoreConfig(queueStoreConfig);

		return config;
	}
}
