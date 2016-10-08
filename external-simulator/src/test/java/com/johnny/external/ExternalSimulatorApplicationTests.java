package com.johnny.external;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
@WebAppConfiguration
@EnableJms
@ActiveProfiles("junit")
public class ExternalSimulatorApplicationTests {

    @Test
    public void contextLoads() {
    }

}
