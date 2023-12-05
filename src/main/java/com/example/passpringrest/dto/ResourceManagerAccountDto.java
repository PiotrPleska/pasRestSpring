package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;


public class ResourceManagerAccountDto extends AbstractAccountDto {

    public ResourceManagerAccountDto() {
    }

    @JsonCreator
    public ResourceManagerAccountDto(@JsonProperty("login") String login, @JsonProperty("password") String password,
                                     @JsonProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public ResourceManagerAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }
}
