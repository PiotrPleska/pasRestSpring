package com.example.passpringrest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("admin")
@Access(AccessType.FIELD)
public class AdminAccount extends AbstractAccount {

    public AdminAccount(String login, String password, String personalId, boolean active) {
        super(login, password, personalId, active);
    }

    public AdminAccount(UUID id, String login, String password, String personalId, boolean active) {
        super(id, login, password, personalId, active);
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
        return true;
    }


    @Override
    public String toString() {
        return "PremiumClient{" + "id=" + getId() + ", firstName='" + getLogin() + '\'' + ", lastName='" + getPassword() + '\'' + ", personalId='" + getPersonalId() + '\'' + '}';
    }
}
