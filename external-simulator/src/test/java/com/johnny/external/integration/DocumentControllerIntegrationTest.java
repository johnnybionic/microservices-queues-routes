package com.johnny.external.integration;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.controller.DocumentController;
import com.johnny.external.domain.Document;

/**
 * This integration test uses actual components (not mocked) with an in-memory
 * database. It ensures that the wiring of components is working as expected -
 * it does not test the components themselves.
 * 
 * @author johnny
 *
 */
@ActiveProfiles({ "junit", "integration" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql") })
public class DocumentControllerIntegrationTest {

	private static final Long DOCUMENT_ID = 1L;

	// no mocks
	@Autowired
	private DocumentController controller;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void thatOneDocumentIsFound() {

		Document documentById = controller.getDocumentById(DOCUMENT_ID);

		assertNotNull(documentById);
	}
}
