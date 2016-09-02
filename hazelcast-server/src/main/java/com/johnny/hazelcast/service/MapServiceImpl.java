package com.johnny.hazelcast.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnny.hazelcast.persistence.MySQLMapStore;

@Service
public class MapServiceImpl implements MapService<String, String> {

	private MySQLMapStore mapStore;

	@Autowired
	public MapServiceImpl(MySQLMapStore mapStore) {
		this.mapStore = mapStore;
	}

	@Override
	public Map<String, String> findAll() {
		Iterable<String> loadAllKeys = mapStore.loadAllKeys();
		
		// why oh why ...
		Collection<String> keys = new ArrayList<>();
		loadAllKeys.forEach(keys::add);
		Map<String, String> loadAll = mapStore.loadAll(keys);
		
		return loadAll;
	}

	@Override
	public String findOne(String id) {
		return mapStore.load(id);
	}

}
