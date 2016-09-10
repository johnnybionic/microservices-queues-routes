package com.johnny.dispatcher.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.dispatcher.domain.DocumentRequest;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;;

/**
 * Converts a document request to and from JSON.
 * 
 * @author johnny
 *
 */
// @Profile({"default", "junit", "apollo"})
@Component
// don't allow 'new', as auto-wiring won't work
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentRequestConverter implements MessageConverter {

    private ObjectMapper mapper;

    /**
     * If the constructor is used for injection, the injected objects can be
     * checked. The same is true of method injection, but that's not guaranteed
     * to be called See
     * https://spring.io/blog/2007/07/11/setter-injection-versus-constructor-
     * injection-and-the-use-of-required/
     * 
     * @param mapper the mapper to use.
     * 
     */
    @Autowired
    public DocumentRequestConverter(final ObjectMapper mapper) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper cannot be null");
        }

        this.mapper = mapper;
    }

    /**
     * Converts incoming message to {@link DocumentRequest}.
     * 
     * @param message the message
     * @throws JMSException oops
     * @throws MessageConversionException oops
     */
    @Override
    public final DocumentRequest fromMessage(final Message message) throws JMSException, MessageConversionException {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();

            try {
                DocumentRequest documentRequest = mapper.readValue(text, DocumentRequest.class);
                return documentRequest;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("Wrong type of message");
    }

    /**
     * Converts outgoing message.
     * 
     * @param source the outgoing message
     * @param session the session
     * @throws JMSException oops
     * @throws MessageConversionException oops
     */
    @Override
    public final Message toMessage(final Object source, final Session session)
            throws JMSException, MessageConversionException {
        try {
            String json = mapper.writeValueAsString(source);
            return session.createTextMessage(json);
        }
        catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }

    }

}
