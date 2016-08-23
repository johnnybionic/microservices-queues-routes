package com.johnny.external.messaging;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.external.DocumentFactory;
import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.Document;
import com.johnny.external.domain.DocumentRequest;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
@WebAppConfiguration
public class DocumentRequestConverterTest {

	@Autowired
	private DocumentRequestConverter converter;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void thatMessageIsRead() {
		
		DocumentRequest documentRequest = DocumentFactory.getDocumentRequest(); 
		String documentJson = null;
		try {
			documentJson = mapper.writeValueAsString(documentRequest);
		}
		catch (JsonProcessingException e1) {
			fail(e1.getMessage());
		}
		
		TextMessage message = mock(TextMessage.class);
		try {
			when(message.getText()).thenReturn(documentJson);
			DocumentRequest fromMessage = converter.fromMessage(message );
			assertNotNull(fromMessage);
			assertTrue(fromMessage.equals(documentRequest));
		}
		catch (MessageConversionException | JMSException e) {
			fail(e.getMessage());
		}
	}

}
