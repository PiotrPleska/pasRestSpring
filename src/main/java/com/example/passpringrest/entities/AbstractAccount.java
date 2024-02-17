package com.example.passpringrest.entities;

import com.example.passpringrest.codecs.MongoUUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


public abstract class AbstractAccount implements UserDetails {

    @BsonProperty("_id")
    private MongoUUID id;


    @BsonProperty("login")
    private String login;


    @BsonProperty("password")
    private String password;


    @BsonProperty("personalId")
    private String personalId;

    @BsonProperty("active")
    private boolean active = true;


    public AbstractAccount(String login, String password, String personalId, boolean active) {
        this.id = new MongoUUID(UUID.randomUUID());
        this.login = login;
        this.password = password;
        this.personalId = personalId;
        this.active = active;
    }

    @BsonCreator
    public AbstractAccount(MongoUUID id, String login, String password, String personalId, boolean active) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.personalId = personalId;
        this.active = active;
    }

    public AbstractAccount() {

    }

    public MongoUUID getId() {
        return id;
    }

    public void setId(MongoUUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public abstract Collection<? extends GrantedAuthority> getAuthorities();

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }


    public String toString() {
        return String.format("%s %s (%s)", login, password, personalId);
    }


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
