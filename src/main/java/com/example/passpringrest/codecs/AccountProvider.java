package com.example.passpringrest.codecs;

import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;


public class AccountProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == AbstractAccount.class || clazz == ClientAccount.class || clazz == ResourceManagerAccount.class || clazz == AdminAccount.class) {
            return (Codec<T>) new AccountCodec(codecRegistry);
        }
        return null;
    }
}
