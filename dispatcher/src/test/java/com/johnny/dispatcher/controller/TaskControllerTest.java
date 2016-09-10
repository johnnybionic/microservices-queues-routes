package com.johnny.dispatcher.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * The TaskController provides a simple interface to the task store.
 * 
 * @author johnny
 *
 */

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTests.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTests.sql") })
public class TaskControllerTest {

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * A list of all tasks - that's all we need.
     * 
     * @throws Exception
     */
    @Test
    public void thatAllDocummentsAreReturned() throws Exception {
        mockMvc.perform(get("/task/all")).andExpect(status().isOk()).andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", iterableWithSize(4))).andExpect(jsonPath("$[0]['id']", equalTo(1)))
                .andExpect(jsonPath("$[0]['name']", containsString("Task one")))
                .andExpect(jsonPath("$[0]['delayPeriod']", equalTo(5000)))
                .andExpect(jsonPath("$[1]['cronExpression']", containsString("0/5***")));

    }

    /**
     * Tests that the date is returned in ISO format. This test the
     * JsonSerialiser that's added for Date types
     * 
     * @throws exception
     */
    @Test
    public void thatDateIsFormattedAsISO() throws Exception {
        mockMvc.perform(get("/task/all")).andExpect(status().isOk()).andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[3]['lastAccessed']", containsString("2016-08-31T15:06:52.69")));

    }

    /**
     * Reset. Doesn't do much in the controller
     * 
     * @throws Exception
     */
    @Test
    public void thatResetWorks() throws Exception {
        mockMvc.perform(get("/task/reset/1")).andExpect(status().isOk());

    }

    /**
     * Suspend a task.
     */
    @Test
    public void thatTaskIsSuspended() throws Exception {
        mockMvc.perform(get("/task/suspend/1/true")).andExpect(status().isOk());

    }

    /**
     * Reinstate a suspended a task.
     * 
     * @throws exception
     */
    @Test
    public void thatTaskIsReinstated() throws Exception {
        mockMvc.perform(get("/task/suspend/1/false")).andExpect(status().isOk());

    }

}
