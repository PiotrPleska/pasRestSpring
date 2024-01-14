package com.example.passpringrest.entities;

import com.example.passpringrest.codecs.MongoUUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@BsonDiscriminator(key = "_clazz", value = "AdminAccount")
public class AdminAccount extends AbstractAccount {


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

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
