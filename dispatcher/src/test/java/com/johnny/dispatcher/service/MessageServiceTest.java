package com.johnny.dispatcher.service;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;
import com.johnny.dispatcher.domain.Task;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceTest {

	private static final Long DOCUMENT_ID = 42L;
	private static final String IDENTIFIER = "corr-id-1";

	@InjectMocks
	private DefaultMessageService service;
	
	@Mock
	private JmsTemplate jmsTemplate;

	@Value("${document.queue.request}") 
	private String documentRequestQueue;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service.setDocumentRequestQueue(documentRequestQueue);
	}
	
	@Test
	public void test() {
		
		Document document = new Document(DOCUMENT_ID, null, null);
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document , null);
		service.sendMessage(documentRequest );
		
		verify(jmsTemplate).send(anyString(), any(MessageCreator.class));
	}

}
