package com.johnny.hazelcast.service;

import com.johnny.hazelcast.persistence.MySQLMapStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapServiceImpl implements MapService<String, String> {

    private final MySQLMapStore mapStore;

    @Autowired
    public MapServiceImpl(final MySQLMapStore mapStore) {
        this.mapStore = mapStore;
    }

    @Override
    public Map<String, String> findAll() {
        final Iterable<String> loadAllKeys = mapStore.loadAllKeys();

        // why oh why ...
        final Collection<String> keys = new ArrayList<>();
        loadAllKeys.forEach(keys::add);
        final Map<String, String> loadAll = mapStore.loadAll(keys);

        return loadAll;
    }

    @Override
    public String findOne(final String id) {
        return mapStore.load(id);
    }

}
