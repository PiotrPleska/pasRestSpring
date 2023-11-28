package com.example.passpringrest.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.GregorianCalendar;

public class GregorianCalendarCodec implements Codec<GregorianCalendar> {

    @Override
    public GregorianCalendar decode(BsonReader reader, DecoderContext decoderContext) {
        long timeInMillis = reader.readDateTime();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }

    @Override
    public void encode(BsonWriter writer, GregorianCalendar value, EncoderContext encoderContext) {
        writer.writeDateTime(value.getTimeInMillis());
    }

    @Override
    public Class<GregorianCalendar> getEncoderClass() {
        return GregorianCalendar.class;
    }
}
