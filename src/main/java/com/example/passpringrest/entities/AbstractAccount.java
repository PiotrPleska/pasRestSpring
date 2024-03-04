package com.example.passpringrest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "account")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="client_type")
public abstract class AbstractAccount implements UserDetails {

    public AbstractAccount(String login, String password, String personalId, boolean active) {
        this.login = login;
        this.password = password;
        this.personalId = personalId;
        this.active = active;
    }

    public AbstractAccount(UUID id, String login, String password, String personalId, boolean active) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.personalId = personalId;
        this.active = active;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;


    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "personal_id", unique = true, nullable = false)
    private String personalId;


    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Version
    private long version;


    @Override
    public abstract Collection<? extends GrantedAuthority> getAuthorities();


    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


    public String toString() {
        return String.format("%s %s (%s)", login, password, personalId);
    }

    public boolean getActive() {
        return active;
    }

}
