package com.example.passpringrest.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public abstract class AbstractAccountDto {

    @JsonbProperty("id")
    private UUID id;

    @NotNull
    @Size(min = 6, max = 20, message = "Login must be between 6 and 20 characters")
    @JsonbProperty("login")
    private String login;

    @NotNull
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @NotNull
    @Size(min = 11, max = 11, message = "Personal id must be 11 characters")
    @JsonbProperty("personalId")
    private String personalId;

    @JsonbProperty("active")
    private boolean active;

    public AbstractAccountDto() {
    }

    @JsonbCreator
    public AbstractAccountDto(@JsonbProperty("login") String login, @JsonbProperty("password") String password,
                              @JsonbProperty("personalId") String personalId) {
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

    public String getPassword() {
        return password;
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
