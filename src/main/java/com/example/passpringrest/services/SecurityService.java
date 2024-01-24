package com.example.passpringrest.services;


import com.example.passpringrest.codecs.MongoUUID;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final RentRepository rentRepository;


    private final RoomRepository roomRepository;


    private final AccountRepository accountRepository;

    public boolean canAccessByAccountId(Authentication authentication, String userId) {
        AbstractAccount account = accountRepository.readAccountByLogin(authentication.getName());
        if(account == null)
            return false;
        return Objects.equals(account.getId().getUuid().toString(), userId);
    }

    public boolean canAccessByPersonalId(Authentication authentication, String personalId) {
        AbstractAccount account = accountRepository.readAccountByLogin(authentication.getName());
        if(account == null)
            return false;
        return Objects.equals(account.getPersonalId(), personalId);
    }

    public boolean canAccessRentByRentId(Authentication authentication, String rentId) {
        Rent rent = rentRepository.readRentById(new MongoUUID(UUID.fromString(rentId)));
        if(rent == null)
            return false;
        return Objects.equals(rent.getClientAccount().getLogin(), authentication.getName());
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
