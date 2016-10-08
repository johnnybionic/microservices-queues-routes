package com.johnny.external.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.external.domain.DocumentRequest;

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
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;;

/**
 * Converts a document request to and from JSON.
 * 
 * @author johnny
 *
 */

@Component
// don't allow 'new', as auto-wiring won't work
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DocumentRequestConverter implements MessageConverter {

    private ObjectMapper mapper;

    /*
     * If the constructor is used for injection, the injected objects can be
     * checked. The same is true of method injection, but that's not guaranteed
     * to be called See
     * https://spring.io/blog/2007/07/11/setter-injection-versus-constructor-
     * injection-and-the-use-of-required/
     * 
     */
    @Autowired
    public DocumentRequestConverter(final ObjectMapper mapper) {
        this();

        if (mapper == null) {
            throw new IllegalArgumentException("Mapper cannot be null");
        }

        this.mapper = mapper;
    }

    @Override
    public DocumentRequest fromMessage(final Message message) throws JMSException, MessageConversionException {

        if (message instanceof TextMessage) {
            final TextMessage textMessage = (TextMessage) message;
            final String text = textMessage.getText();

            try {
                final DocumentRequest documentRequest = mapper.readValue(text, DocumentRequest.class);
                return documentRequest;
            }
            catch (final IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Wrong type of message");
    }

    @Override
    public Message toMessage(final Object source, final Session session)
            throws JMSException, MessageConversionException {
        try {
            final String json = mapper.writeValueAsString(source);
            return session.createTextMessage(json);
        }
        catch (final JsonProcessingException e) {
            log.error(e.getMessage());
            throw new MessageConversionException(e.getMessage());
        }

    }

}
