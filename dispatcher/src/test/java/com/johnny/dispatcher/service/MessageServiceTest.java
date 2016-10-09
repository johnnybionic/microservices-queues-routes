package com.johnny.dispatcher.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
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

    @Spy
    private JmsTemplate jmsTemplate;

    @Mock
    private MessageConverter convertor;

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

        doNothing().when(jmsTemplate).send(anyString(), any(MessageCreator.class));

        service.sendMessage(documentRequest);

        verify(jmsTemplate).send(anyString(), any(MessageCreator.class));
    }

    /**
     * The MessageCreator used is an anonymous inner class (lambda). It would be
     * a shame to get rid of the benefit and readability of the lambda, e.g. by
     * using a separate class.
     * 
     * @throws JMSException
     * @throws MessageConversionException
     */
    @Test
    public void canTheMessageCreatorBeTested() throws MessageConversionException, JMSException {

        final Document document = new Document(DOCUMENT_ID, null, null);
        final DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document, null);
        final Session session = mock(Session.class);

        doAnswer(invocation -> {
            final Object[] args = invocation.getArguments();
            final MessageCreator arg = (MessageCreator) args[1];
            Message createMessage = arg.createMessage(session);
            return createMessage;
        }).when(jmsTemplate).send(any(String.class), any(MessageCreator.class));

        service.sendMessage(documentRequest);

        verify(jmsTemplate).send(anyString(), any(MessageCreator.class));

        verify(convertor).toMessage(documentRequest, session);
    }
}
