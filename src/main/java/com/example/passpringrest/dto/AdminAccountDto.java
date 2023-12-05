package com.example.passpringrest.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AdminAccountDto extends AbstractAccountDto {

    public AdminAccountDto() {
    }

    @JsonCreator
    public AdminAccountDto(@JsonProperty("login") String login, @JsonProperty("password") String password,
                           @JsonProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public AdminAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }
}
