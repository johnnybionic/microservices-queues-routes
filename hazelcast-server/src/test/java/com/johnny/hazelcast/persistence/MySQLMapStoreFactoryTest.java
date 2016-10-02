package com.johnny.hazelcast.persistence;

import com.hazelcast.core.MapLoader;
import com.johnny.hazelcast.HazelcastServerApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HazelcastServerApplication.class)
public class MySQLMapStoreFactoryTest {

    private static final String MAP_NAME = "map-name";

    @Autowired
    private MySQLMapStoreFactory factory;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void thatMapLoaderIsReturned() {
        Assert.notNull(factory);
        final MapLoader<String, String> newMapStore = factory.newMapStore(MAP_NAME, null);
        Assert.notNull(newMapStore);

    }

}
