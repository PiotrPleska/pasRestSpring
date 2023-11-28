package com.example.passpringrest.codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoUUIDCodecProvider implements CodecProvider {

    public MongoUUIDCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == MongoUUID.class) {
            return (Codec<T>) new MongoUUIDCodec(codecRegistry);
        }
        return null;
    }
}
