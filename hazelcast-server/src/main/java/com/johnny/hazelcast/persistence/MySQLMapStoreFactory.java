package com.johnny.hazelcast.persistence;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MySQLMapStoreFactory implements MapStoreFactory<String, String> {

    private final MapLoader<String, String> mapStore;

    @Autowired
    public MySQLMapStoreFactory(final MapLoader<String, String> mapStore) {
        this.mapStore = mapStore;
    }

    @Override
    public MapLoader<String, String> newMapStore(final String mapName, final Properties properties) {
        log.info("New map store with name {}", mapName);
        return mapStore;
    }

}
