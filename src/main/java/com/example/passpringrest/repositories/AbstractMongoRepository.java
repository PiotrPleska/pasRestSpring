package com.example.passpringrest.repositories;

import com.example.passpringrest.codecs.AccountProvider;
import com.example.passpringrest.codecs.GregorianCalendarCodecProvider;
import com.example.passpringrest.codecs.MongoUUIDCodecProvider;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private static final ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018," + "mongodb3:27019" +
            "/?replicaSet=replica_set_single");

    private final MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).conventions(List.of(Conventions.ANNOTATION_CONVENTION,
                    Conventions.CLASS_AND_PROPERTY_CONVENTION)).build());

    protected MongoClient mongoClient;
    protected MongoDatabase db;

    public MongoDatabase initDbConnection() {
        MongoClientSettings settings =
                MongoClientSettings.builder().credential(credential)
                        .applyConnectionString(connectionString)
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .codecRegistry(
                                CodecRegistries.fromRegistries(
                                        CodecRegistries.fromProviders(new MongoUUIDCodecProvider()),
                                        CodecRegistries.fromProviders(new AccountProvider()),
                                        CodecRegistries.fromProviders(new GregorianCalendarCodecProvider()),
                                        MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry))
                        .build();

        mongoClient = MongoClients.create(settings);
        return db = mongoClient.getDatabase("pasdb");
    }

    public void close() {
        mongoClient.close();
    }
}
