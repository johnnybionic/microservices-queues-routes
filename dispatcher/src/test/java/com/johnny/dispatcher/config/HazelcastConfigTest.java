package com.johnny.dispatcher.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This version of the test uses a 'fake' to extend the unit under test, and
 * override methods that will provide mocks.
 * 
 * The test itself can extend the unit, but here it's done with a separate
 * class.
 * 
 * @author johnny
 *
 */

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HazelcastConfigTest {

    private FakeHazelcastConfig config;

    @Autowired
    private ApplicationConfig applicationConfig;

    private ClientConfig clientConfig;

    public HazelcastInstance hazelcastInstance;

    @Before
    public void setUp() throws Exception {
        config = new FakeHazelcastConfig(applicationConfig);
        clientConfig = mock(ClientConfig.class);
        hazelcastInstance = mock(HazelcastInstance.class);
    }

    @Test
    public void testThatConfigIsReturned() {
        ClientNetworkConfig networkConfig = new ClientNetworkConfig();
        when(clientConfig.getNetworkConfig()).thenReturn(networkConfig);

        GroupConfig groupConfig = new GroupConfig();
        when(clientConfig.getGroupConfig()).thenReturn(groupConfig);

        HazelcastInstance hazelcastInstance = config.hazelcastInstance();
        assertNotNull(hazelcastInstance);

        assertTrue(networkConfig.getAddresses().contains(applicationConfig.getHazelcasetServerAddress()));
        assertEquals(applicationConfig.getHazelcastGroupPassword(), groupConfig.getPassword());
        assertEquals(applicationConfig.getHazelcastGroupServer(), groupConfig.getName());
    }

    class FakeHazelcastConfig extends HazelcastConfig {

        public FakeHazelcastConfig(ApplicationConfig applicationConfig) {
            super(applicationConfig);
        }

        @Override
        protected HazelcastInstance getHazelcastInstance(ClientConfig clientConfig) {
            return hazelcastInstance;
        }

        @Override
        protected ClientConfig getClientConfig() {
            return clientConfig;
        }
    }
}
