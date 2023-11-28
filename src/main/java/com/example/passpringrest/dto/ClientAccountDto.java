package com.example.passpringrest.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbCreator;

import java.util.UUID;

public class ClientAccountDto extends AbstractAccountDto {

    public ClientAccountDto() {
    }

    @JsonbCreator
    public ClientAccountDto(@JsonbProperty("login") String login, @JsonbProperty("password") String password,
                            @JsonbProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public ClientAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }
}
