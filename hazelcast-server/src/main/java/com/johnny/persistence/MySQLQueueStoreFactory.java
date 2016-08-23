package com.johnny.persistence;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hazelcast.core.QueueStore;
import com.hazelcast.core.QueueStoreFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Factory for QueueStore. As each queue is stored with a particular column name,
 * they each require their own QueueStore, so these are defined as prototype. Each
 * time the factory method is called, a new store is created.
 * 
 * @author johnny
 *
 */
@Slf4j
@Service
public class MySQLQueueStoreFactory implements QueueStoreFactory<String> {

    private ApplicationContext applicationContext;
    
    @Autowired
    public MySQLQueueStoreFactory(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
    }
    
	@Override
	public QueueStore<String> newQueueStore(String name, Properties properties) {
    
		log.debug("newQueueStore({}, {})", name, properties);

		// each instance returned will be new (prototype)
		MySQLQueueStore store = applicationContext.getBean(MySQLQueueStore.class);
		store.setQueueName(name);

        return store;
	}

}
