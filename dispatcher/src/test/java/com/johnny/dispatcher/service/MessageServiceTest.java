package com.johnny.dispatcher.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private static final Long DOCUMENT_ID = 42L;
    private static final String IDENTIFIER = "corr-id-1";

    @InjectMocks
    private MessageServiceJms service;

    @Mock
    private JmsTemplate jmsTemplate;

    @Value("${document.queue.request}")
    private String documentRequestQueue;

    @Before
    public void setUp() {
        service.setDocumentRequestQueue(documentRequestQueue);
    }

    @Test
    public void test() {

        final Document document = new Document(DOCUMENT_ID, null, null);
        final DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document, null);
        service.sendMessage(documentRequest);

        verify(jmsTemplate).send(anyString(), any(MessageCreator.class));
    }

}
