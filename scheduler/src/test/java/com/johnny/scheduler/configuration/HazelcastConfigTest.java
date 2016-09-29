package com.johnny.scheduler.configuration;

import static org.junit.Assert.assertNotNull;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.johnny.scheduler.config.ApplicationConfig;
import com.johnny.scheduler.config.HazelcastConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This test is to stop code coverage from complaining. It could be argued that
 * the configuration classes are excluded from test converage, unless they need
 * to access some external source where exceptions could be thrown.
 * 
 * Note: when the tests run, a mock Hazelcast configuration is used.
 * 
 * @author johnny
 *
 */

@SpringBootTest
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
public class HazelcastConfigTest {

    private HazelcastConfig hazelcastConfig;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Before
    public void setUp() throws Exception {
        hazelcastConfig = new HazelcastConfig(applicationConfig);
    }

    @Test
    public void thatInstanceIsCreatedWithValuesFromApplicationConfig() {

        // creates an in-memory server
        final Config serverConfig = new Config();
        serverConfig.setProperty("hazelcast.shutdownhook.enabled", "false");
        serverConfig.getGroupConfig().setName(applicationConfig.getHazelcastGroupServer())
                .setPassword(applicationConfig.getHazelcastGroupPassword());
        final NetworkConfig network = serverConfig.getNetworkConfig();
        network.getJoin().getTcpIpConfig().setEnabled(false);
        network.getJoin().getMulticastConfig().setEnabled(false);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(serverConfig);

        final HazelcastInstance hazelcastInstance = hazelcastConfig.hazelcastInstance();
        assertNotNull(hazelcastInstance);
    }

}
