package com.example.passpringrest.dto;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.UUID;

public class ResourceManagerAccountDto extends AbstractAccountDto {

    public ResourceManagerAccountDto() {
    }

    @JsonbCreator
    public ResourceManagerAccountDto(@JsonbProperty("login") String login, @JsonbProperty("password") String password,
                                     @JsonbProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public ResourceManagerAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }
}
