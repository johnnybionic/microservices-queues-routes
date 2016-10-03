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
 * the configuration classes are excluded from test coverage, unless they need
 * to access some external source where exceptions could be thrown.
 * 
 * Note: when other tests run that write to the queues, a mock Hazelcast
 * configuration is used.
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

    /**
     * I don't think this test gives much value, but it shows how to create an
     * embedded Hazelcast server. This module doesn't read and write from queues
     * - it only writes - so a mock is sufficient for tests.
     * 
     * An embedded server would be useful for integration tests.
     */
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

        hazelcastInstance.shutdown();
        instance.shutdown();
    }

}
