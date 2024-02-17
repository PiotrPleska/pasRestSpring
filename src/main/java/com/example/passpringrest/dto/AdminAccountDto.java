package com.example.passpringrest.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AdminAccountDto extends AbstractAccountDto {

    public AdminAccountDto() {
    }


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
        return false;
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
