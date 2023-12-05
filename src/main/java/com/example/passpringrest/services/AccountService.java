package com.example.passpringrest.services;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.converters.AccountConverter;
import com.example.passpringrest.dto.AbstractAccountDto;
import com.example.passpringrest.dto.AdminAccountDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.dto.ResourceManagerAccountDto;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accoutRepository;

    public List<AbstractAccountDto> readAllAccounts() {
        List<AbstractAccount> accounts = accoutRepository.readAllAccounts();
        List<AbstractAccountDto> accountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            accountDtos.add(AccountConverter.solveAccountDtoType(account));
        }
        return accountDtos;
    }

    public List<AbstractAccountDto> readAdminAccounts() {
        List<AbstractAccount> accounts = accoutRepository.readAdminAccounts();
        List<AbstractAccountDto> adminAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            adminAccountDtos.add(AccountConverter.toAdminDto((AdminAccount) account));
        }
        return adminAccountDtos;
    }

    public List<AbstractAccountDto> readResourceManagerAccounts() {
        List<AbstractAccount> accounts = accoutRepository.readResourceManagerAccounts();
        List<AbstractAccountDto> resourceManagerAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            resourceManagerAccountDtos.add(AccountConverter.toResourceManagerDto((ResourceManagerAccount) account));
        }
        return resourceManagerAccountDtos;
    }

    public List<AbstractAccountDto> readClientAccounts() {
        List<AbstractAccount> accounts = accoutRepository.readClientAccounts();
        List<AbstractAccountDto> clientAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            clientAccountDtos.add(AccountConverter.toClientDto((ClientAccount) account));
        }
        return clientAccountDtos;
    }

    public AbstractAccountDto readAccountById(String id) throws ResourceNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            MongoUUID mongoUUID = new MongoUUID(uuid);
            AbstractAccount account = accoutRepository.readAccountById(mongoUUID);
            return AccountConverter.solveAccountDtoType(account);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Invalid id");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public AbstractAccountDto readAccountByPersonalId(String personalId) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accoutRepository.readAccountByPersonalId(personalId);
            return AccountConverter.solveAccountDtoType(account);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Invalid personal id");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }

    }

    public AbstractAccountDto readAccountByLogin(String login) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accoutRepository.readAccountByLogin(login);
            return AccountConverter.solveAccountDtoType(account);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Invalid login");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public List<AbstractAccountDto> readAccountsByPartOfLogin(String partOfLogin) throws ResourceNotFoundException {
        List<AbstractAccount> accounts = accoutRepository.readAccountsByPartOfLogin(partOfLogin);
        if(accounts.isEmpty()) throw new ResourceNotFoundException("No accounts with given login");
        List<AbstractAccountDto> accountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            accountDtos.add(AccountConverter.solveAccountDtoType(account));
        }
        return accountDtos;
    }

    public AbstractAccountDto updateAccountPasswordByLogin(String login, ClientAccountDto clientDto) throws ResourceNotFoundException {
        try {
            readAccountByLogin(login);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + login + " not found");
        }
        accoutRepository.updateAccountPasswordByLogin(login, clientDto.getPassword());
        AbstractAccount account = accoutRepository.readAccountByLogin(login);
        return AccountConverter.solveAccountDtoType(account);
    }

    public AbstractAccountDto updateAccountActiveByLogin(String login, boolean active) throws ResourceNotFoundException {
        try {
            readAccountByLogin(login);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + login + " not found");
        }
        accoutRepository.updateAccountActiveByLogin(login, active);
        AbstractAccount account = accoutRepository.readAccountByLogin(login);
        return AccountConverter.solveAccountDtoType(account);
    }

    public ClientAccountDto createClientAccount(ClientAccountDto clientAccountDto) throws ResourceOccupiedException {
        AbstractAccount existingAccount = accoutRepository.readAccountByLoginWithoutException(clientAccountDto.getLogin());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accoutRepository.readAccountByPersonalIdWithoutException(clientAccountDto.getPersonalId());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        ClientAccount clientAccount = AccountConverter.toClient(clientAccountDto);
        accoutRepository.insertAccount(clientAccount);
        return AccountConverter.toClientDto(clientAccount);
    }

    public AdminAccountDto createAdminAccount(AdminAccountDto adminAccountDto) throws ResourceOccupiedException {
        AbstractAccount existingAccount = accoutRepository.readAccountByLoginWithoutException(adminAccountDto.getLogin());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accoutRepository.readAccountByPersonalIdWithoutException(adminAccountDto.getPersonalId());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        var admin = AccountConverter.toAdmin(adminAccountDto);
        var dtoAdmin = AccountConverter.toAdminDto(admin);
        accoutRepository.insertAccount(admin);
        return dtoAdmin;
    }

    public ResourceManagerAccountDto createResourceManagerAccount(ResourceManagerAccountDto resourceManagerAccountDto) throws ResourceOccupiedException {
        AbstractAccount existingAccount = accoutRepository.readAccountByLoginWithoutException(resourceManagerAccountDto.getLogin());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accoutRepository.readAccountByPersonalIdWithoutException(resourceManagerAccountDto.getPersonalId());
        if (existingAccount != null) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        ResourceManagerAccount resourceManagerAccount = AccountConverter.toResourceManager(resourceManagerAccountDto);
        accoutRepository.insertAccount(resourceManagerAccount);
        return AccountConverter.toResourceManagerDto(resourceManagerAccount);
    }
}
