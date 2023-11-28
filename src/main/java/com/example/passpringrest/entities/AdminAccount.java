package com.example.passpringrest.entities;

import com.example.passpringrest.codecs.MongoUUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "AdminAccount")
public class AdminAccount extends AbstractAccount {


    public AdminAccount(String login, String password, String personalId, boolean active) {
        super(login, password, personalId, active);
    }

    @BsonCreator
    public AdminAccount(@BsonProperty("id") MongoUUID id, @BsonProperty("login") String login, @BsonProperty("password") String password,
                        @BsonProperty("personalId") String personalId, @BsonProperty("active") boolean active) {
        super(id, login, password, personalId, active);
    }

    public AdminAccount() {

    }

    @Override
    public String toString() {
        return "PremiumClient{" + "id=" + getId() + ", firstName='" + getLogin() + '\'' + ", lastName='" + getPassword() + '\'' + ", personalId='" + getPersonalId() + '\'' + '}';
    }
}
