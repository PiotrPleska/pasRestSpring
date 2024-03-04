package com.example.passpringrest.services;


import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.Rent;
import com.example.passpringrest.repositories.AccountRepository;
import com.example.passpringrest.repositories.RentRepository;
import com.example.passpringrest.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final RentRepository rentRepository;

    private final AccountRepository accountRepository;

    public boolean canAccessByAccountId(Authentication authentication, String userId) {
        Optional<AbstractAccount> account = accountRepository.findByLogin(authentication.getName());
        if (account.isEmpty())
            return false;
        return Objects.equals(account.get().getId().toString(), userId);
    }

    public boolean canAccessByPersonalId(Authentication authentication, String personalId) {
        Optional<AbstractAccount> account = accountRepository.findByLogin(authentication.getName());
        if (account.isEmpty())
            return false;
        return Objects.equals(account.get().getPersonalId(), personalId);
    }

    public boolean canAccessRentByRentId(Authentication authentication, String rentId) {
        Optional<Rent> rent = rentRepository.findById(UUID.fromString(rentId));
        if (rent.isEmpty())
            return false;
        return Objects.equals(rent.get().getClientAccount().getLogin(), authentication.getName());
    }

    public String generateEtag(String login) {
        String dataToHash = "MostSecretKeyEver" + login;

        String hashedData = DigestUtils.md5DigestAsHex(dataToHash.getBytes());

        return "\"" + hashedData + "\"";
    }

    public boolean checkEtag(String etag, String login) {
        String storedEtag = generateEtag(login);
        return etag != null && etag.equals(storedEtag);
    }

}
