package com.johnny.dispatcher.messaging;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.dispatcher.DocumentFactory;
import com.johnny.dispatcher.domain.DocumentRequest;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests {@link DocumentRequestConverter}.
 * 
 * Refer to the same test in the 'external' module, that uses wiring. This test
 * does the same thing without all the overhead.
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
public class DocumentRequestConverterTest {

    private DocumentRequestConverter converter;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = spy(new ObjectMapper());
        converter = new DocumentRequestConverter(mapper);
    }

    @Test
    public void thatMessageIsRead() {

        final DocumentRequest documentRequest = DocumentFactory.getDocumentRequest();
        String documentJson = null;
        try {
            documentJson = mapper.writeValueAsString(documentRequest);
        }
        catch (final JsonProcessingException e1) {
            fail(e1.getMessage());
        }

        final TextMessage message = mock(TextMessage.class);
        try {
            when(message.getText()).thenReturn(documentJson);
            final DocumentRequest fromMessage = converter.fromMessage(message);
            assertNotNull(fromMessage);
            assertTrue(fromMessage.equals(documentRequest));
        }
        catch (MessageConversionException | JMSException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void whenBadMessageIsReceivedThenThrowException() throws MessageConversionException, JMSException {
        converter.fromMessage(mock(Message.class));
    }

    /**
     * Code coverage showed that exceptions thrown by the ObjectMapper were not
     * covered, so this test simulates such an exception using a Spy.
     * 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws JMSException
     */
    @Test(expected = RuntimeException.class)
    public void whenMapperFailsToConvertThenExceptionThrown_UsingSpy()
            throws JsonParseException, JsonMappingException, IOException, JMSException {
        final DocumentRequest documentRequest = DocumentFactory.getDocumentRequest();
        String documentJson = null;
        try {
            documentJson = mapper.writeValueAsString(documentRequest);
        }
        catch (final JsonProcessingException e1) {
            fail(e1.getMessage());
        }

        final TextMessage message = mock(TextMessage.class);

        doThrow(IOException.class).when(mapper).readValue(documentJson, DocumentRequest.class);

        when(message.getText()).thenReturn(documentJson);
        converter.fromMessage(message);
    }

    /**
     * A far simpler way to get the Mapper to thrown an exception :)
     * 
     * @throws JMSException
     */
    @Test(expected = RuntimeException.class)
    public void whenMapperFailsToConvertThenExceptionThrown_BadInput() throws JMSException {
        String documentJson = "{bad:input...zzzzzz:";

        final TextMessage message = mock(TextMessage.class);

        when(message.getText()).thenReturn(documentJson);
        converter.fromMessage(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMapperIsNull() {
        new DocumentRequestConverter(null);
    }

    @Test
    public void thatToMessageReturnsMessage() throws MessageConversionException, JMSException, JsonProcessingException {
        final DocumentRequest documentRequest = DocumentFactory.getDocumentRequest();
        String json = mapper.writeValueAsString(documentRequest);

        Session session = mock(Session.class);
        TextMessage textMessage = mock(TextMessage.class);
        when(session.createTextMessage(json)).thenReturn(textMessage);

        Message message = converter.toMessage(documentRequest, session);

        verify(session).createTextMessage(json);
        assertTrue(message == textMessage);
    }

    @Test(expected = MessageConversionException.class)
    public void whenMapperThrowsExpeptionInToMessage()
            throws JsonProcessingException, MessageConversionException, JMSException {

        final DocumentRequest documentRequest = DocumentFactory.getDocumentRequest();
        Session session = mock(Session.class);

        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any());
        Message message = converter.toMessage(documentRequest, session);

    }
}
