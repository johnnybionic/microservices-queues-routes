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

    @Override
    public final void serialize(final Date value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        // using a static format gets marked as not threadsafe. Doubt it will be
        // a problem, but anyway ...
        final String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS").format(value);
        gen.writeString(formattedDate);
    }

}
