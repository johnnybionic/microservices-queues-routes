package com.johnny.scheduler.routes;

import static org.junit.Assert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.scheduler.routes.QuartzRouteBuilder;

import lombok.Getter;

@SpringBootTest
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
public class QuartzRouteBuilderTest {

    private static final String GROUP_NAME = "group";
    private static final String TIMER_NAME = "timer";
    private static final String INFO = "info";
    private static final String CRON = "trigger.test&fireNow=true&trigger.repeatCount=1";
    private QuartzRouteBuilder route;

    @Autowired
    private CamelContext camelContext;

    /**
     * Uses a simple trigger with fireNow. Tests that the processor is called
     * when the timer fires.
     * 
     * @throws Exception
     */
    @Test
    public void happyPath() throws Exception {

        TestProcessor processor = new TestProcessor();

        route = new QuartzRouteBuilder(GROUP_NAME, TIMER_NAME, INFO, CRON, processor);
        camelContext.addRoutes(route);

        // let's give Camel a chance to get going
        Thread.sleep(1000);

        System.out.println("***** processor is :" + processor);
        if (processor != null) {
            System.out.println("***** " + processor.isProcessCalled());
        }
        assertTrue(processor.isProcessCalled());
    }

    class TestProcessor implements Processor {

        @Getter
        boolean processCalled;

        @Override
        public void process(Exchange exchange) throws Exception {
            processCalled = true;
        }

    }
}
