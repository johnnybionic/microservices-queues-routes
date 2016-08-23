package com.johnny.external.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.external.DocumentFactory;
import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;
import com.johnny.external.messaging.DocumentRequestReceiver;

/**
 * Tests the messaging is wired correctly, using an embedded ActiveMQ instance.
 *  
 * @author johnny
 *
 */

@ActiveProfiles({ "junit", "integration" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
//@SqlGroup({ @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
//		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql") })

public class MessagingIntegrationTest {

	private static final String DOCUMENT_REQUEST_QUEUE = "document.request.queue";
	private static final String DOCUMENT_REQUEST_QUEUE_ACK = "document.request.ack.queue";

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private JmsTemplate template;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testReceiveDocumentMessage() throws JMSException, JsonProcessingException {
		
		DocumentRequest documentRequest = DocumentFactory.getDocumentRequest();
		String documentJson = mapper.writeValueAsString(documentRequest);
		
		template.send(DOCUMENT_REQUEST_QUEUE, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(documentJson);
			}
		});
		
		Message receive = template.receive(DOCUMENT_REQUEST_QUEUE_ACK);
		assertNotNull(receive);
		if (receive instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) receive;
			String text = textMessage.getText();
			// the message is surrounded by quotes
			assertTrue(text.contains(DocumentRequestReceiver.ACKNOWLEDGED));
		}
	}
}
