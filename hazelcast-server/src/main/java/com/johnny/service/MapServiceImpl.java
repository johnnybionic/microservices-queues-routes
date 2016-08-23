package com.johnny.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnny.persistence.MySQLMapStore;

@Service
public class MapServiceImpl implements MapService<Long, String> {

	private MySQLMapStore mapStore;

	@Autowired
	public MapServiceImpl(MySQLMapStore mapStore) {
		this.mapStore = mapStore;
	}

	@Override
	public Map<Long, String> findAll() {
		Iterable<Long> loadAllKeys = mapStore.loadAllKeys();
		
		// why oh why ...
		Collection<Long> keys = new ArrayList<>();
		loadAllKeys.forEach(keys::add);
		Map<Long, String> loadAll = mapStore.loadAll(keys);
		
		return loadAll;
	}

	@Override
	public String findOne(Long id) {
		return mapStore.load(id);
	}

}
