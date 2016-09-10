package com.johnny.dispatcher.serialise;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts a Date to ISO format.
 * 
 * @author johnny
 *
 */
public class JsonDateSerialiser extends JsonSerializer<Date> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");

    @Override
    public final void serialize(final Date value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        String formattedDate = DATE_FORMAT.format(value);
        gen.writeString(formattedDate);
    }

}
