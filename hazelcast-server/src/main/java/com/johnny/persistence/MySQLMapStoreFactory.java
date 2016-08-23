package com.johnny.persistence;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStore;
import com.hazelcast.core.MapStoreFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MySQLMapStoreFactory implements MapStoreFactory<Long, String> {

	private final MapLoader<Long, String> mapStore;
	
	@Autowired
	public MySQLMapStoreFactory(MapLoader<Long, String> mapStore) {
		this.mapStore = mapStore;
	}
	
	@Override
	public MapLoader<Long, String> newMapStore(String mapName, Properties properties) {
		log.info("New map store with name {}", mapName);
		return mapStore;
	}

}
