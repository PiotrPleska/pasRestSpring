package com.example.passpringrest.dto;



import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbCreator;

import java.util.UUID;

public class AdminAccountDto extends AbstractAccountDto {

    public AdminAccountDto() {
    }

    @JsonbCreator
    public AdminAccountDto(@JsonbProperty("login") String login, @JsonbProperty("password") String password,
                           @JsonbProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public AdminAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }
}
