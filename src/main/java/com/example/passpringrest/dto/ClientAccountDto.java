package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ClientAccountDto extends AbstractAccountDto {

    public ClientAccountDto() {
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

    @JsonCreator
    public ClientAccountDto(@JsonProperty("login") String login, @JsonProperty("password") String password,
                            @JsonProperty("personalId") String personalId) {
        super(login, password, personalId);
    }

    public ClientAccountDto(UUID id, String login, String personalId, boolean active) {
        super(id, login, personalId, active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }
}
