package com.johnny.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.MapStoreFactory;
import com.hazelcast.core.QueueStoreFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hazelcast configuration.
 * 
 * https://opencredo.com/spring-booting-hazelcast/
 * 
 * http://docs.hazelcast.org/docs/3.5/manual/html/usingwildcard.html
 * 
 * Documentation for queue persistence is a bit lacking, but it can be worked
 * out by looking at map persistence:
 * 
 * http://docs.hazelcast.org/docs/3.5/manual/html/map-persistence.html
 * 
 * @author johnny
 *
 */
@Configuration
public class HazelcastConfiguration {

    @Value("${hazelcast.group:hazelcast-group}")
    private String groupName;

    @Value("${hazelcast.password:hazelcast-password}")
    private String password;

    @Value("${hazelcast.port:5701}")
    private int clientPort;

    /**
     * Configure the instance. Spring starts the instance automatically if a
     * {@link Config} bean is instantiated.
     * 
     * http://docs.hazelcast.org/docs/3.3/manual/html/config.html
     * 
     * @param queueStoreFactory factory that provides QueueStore
     * @param mapStoreFactory factory that provides MapStore
     * @return the server's configuration.
     */
    @Bean
    @Autowired
    public Config getHazelcastConfig(final QueueStoreFactory<String> queueStoreFactory,
            final MapStoreFactory<String, String> mapStoreFactory) {

        final Config config = new Config();
        config.getGroupConfig().setName(groupName).setPassword(password);
        config.getNetworkConfig().setPort(clientPort);

        // configure the map storage - it's the same for every map, hence
        // wildcard
        final MapConfig mapConfig = config.getMapConfig("*");
        mapConfig.setBackupCount(1);
        final MapStoreConfig mapStoreConfig = new MapStoreConfig().setEnabled(true).setProperty("binary", "false")
                .setFactoryImplementation(mapStoreFactory);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        // configure the queue storage
        // configure each queue starting with "queue"
        final QueueConfig queueConfig = config.getQueueConfig("queue.*");
        queueConfig.setBackupCount(1);

        // configure the queue's store
        final QueueStoreConfig queueStoreConfig = new QueueStoreConfig().setEnabled(true).setProperty("binary", "false")
                .setFactoryImplementation(queueStoreFactory);

        queueConfig.setQueueStoreConfig(queueStoreConfig);

        return config;
    }
}
