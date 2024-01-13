package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


@AllArgsConstructor
public abstract class AbstractAccountDto implements UserDetails {

    @JsonProperty("id")
    private UUID id;

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @NotNull
    @Size(min = 6, max = 20, message = "Login must be between 6 and 20 characters")
    @JsonProperty("login")
    private String login;

    @NotNull
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @NotNull
    @Size(min = 11, max = 11, message = "Personal id must be 11 characters")
    @JsonProperty("personalId")
    private String personalId;

    @JsonProperty("active")
    private boolean active;

    public AbstractAccountDto() {
    }

    @JsonCreator
    public AbstractAccountDto(@JsonProperty("login") String login, @JsonProperty("password") String password,
                              @JsonProperty("personalId") String personalId) {
        this.id = UUID.randomUUID();
        this.login = login;
        this.password = password;
        this.personalId = personalId;
        this.active = true;
    }

    public AbstractAccountDto(UUID id, String login, String personalId, boolean active) {
        this.id = id;
        this.login = login;
        this.personalId = personalId;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
        return this.login;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
