package com.example.passpringrest.entities;

import com.example.passpringrest.codecs.MongoUUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "ResourceManagerAccount")
public class ResourceManagerAccount extends AbstractAccount {


    public ResourceManagerAccount(String login, String password, String personalId, boolean active) {
        super(login, password, personalId, active);
    }

    @BsonCreator
    public ResourceManagerAccount(@BsonProperty("_id") MongoUUID id, @BsonProperty("login") String login, @BsonProperty("password") String password
            , @BsonProperty("personalId") String personalId, @BsonProperty("active") boolean active) {
        super(id, login, password, personalId, active);
    }

    public ResourceManagerAccount() {

    }

    @Override
    public String toString() {
        return "RegularClient{" + "id=" + getId() + ", firstName='" + getLogin() + '\'' + ", lastName='" + getPassword() + '\'' + ", personalId='" + getPersonalId() + '\'' + '}';
    }

}
