package com.example.passpringrest.services;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AbstractAccountDto> readAllAccounts() {
        List<AbstractAccount> accounts = accountRepository.findAll();
        List<AbstractAccountDto> accountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            accountDtos.add(AccountConverter.solveAccountDtoType(account));
        }
        return accountDtos;
    }

    public List<AbstractAccountDto> readAdminAccounts() {
        List<AbstractAccount> accounts = accountRepository.findByDiscriminatorValue("admin");
        List<AbstractAccountDto> adminAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            adminAccountDtos.add(AccountConverter.toAdminDto((AdminAccount) account));
        }
        return adminAccountDtos;
    }

    public List<AbstractAccountDto> readResourceManagerAccounts() {
        List<AbstractAccount> accounts = accountRepository.findByDiscriminatorValue("resource_manager");
        List<AbstractAccountDto> resourceManagerAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            resourceManagerAccountDtos.add(AccountConverter.toResourceManagerDto((ResourceManagerAccount) account));
        }
        return resourceManagerAccountDtos;
    }

    public List<AbstractAccountDto> readClientAccounts() {
        List<AbstractAccount> accounts = accountRepository.findByDiscriminatorValue("client");
        List<AbstractAccountDto> clientAccountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            clientAccountDtos.add(AccountConverter.toClientDto((ClientAccount) account));
        }
        return clientAccountDtos;
    }

    public AbstractAccountDto readAccountById(String id) throws ResourceNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            AbstractAccount account = accountRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Account with given id not " +
                    "found"));
            return AccountConverter.solveAccountDtoType(account);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public AbstractAccountDto readAccountByPersonalId(String personalId) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accountRepository.findByPersonalId(personalId).orElseThrow(() -> new ResourceNotFoundException("Account with " +
                    "given personal id not found"));
            return AccountConverter.solveAccountDtoType(account);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }

    }

    public AbstractAccountDto readAccountByLogin(String login) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accountRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Account with given " +
                    "login not found"));
            return AccountConverter.solveAccountDtoType(account);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public List<AbstractAccountDto> readAccountsByPartOfLogin(String partOfLogin) throws ResourceNotFoundException {
        List<AbstractAccount> accounts = accountRepository.findByPartOfLogin(partOfLogin);
        if (accounts.isEmpty()) throw new ResourceNotFoundException("No accounts with given login");
        List<AbstractAccountDto> accountDtos = new ArrayList<>();
        for (AbstractAccount account : accounts) {
            accountDtos.add(AccountConverter.solveAccountDtoType(account));
        }
        return accountDtos;
    }

    public AbstractAccountDto updateClientAccountPasswordByLogin(String login, ClientAccountDto clientDto) throws ResourceNotFoundException {
        AbstractAccount account = accountRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Account with given login " +
                "not found"));
        account.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        accountRepository.save(account);
        return AccountConverter.solveAccountDtoType(account);
    }


    public AbstractAccountDto updateAdminAccountPasswordByLogin(String login, AdminAccountDto adminDto) throws ResourceNotFoundException {
        AbstractAccount account = accountRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Account with given login " +
                "not found"));
        account.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        accountRepository.save(account);
        return AccountConverter.solveAccountDtoType(account);
    }

    public AbstractAccountDto updateResourceManagerAccountPasswordByLogin(String login, ResourceManagerAccountDto resourceManagerDto) throws
    ResourceNotFoundException {
        AbstractAccount account = accountRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Account with given login " +
                "not found"));
        account.setPassword(passwordEncoder.encode(resourceManagerDto.getPassword()));
        accountRepository.save(account);
        return AccountConverter.solveAccountDtoType(account);
    }

    public void updateAccountActiveByLogin(String login, boolean active) throws ResourceNotFoundException {
        AbstractAccount account = accountRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Account with given login " +
                "not found"));
        account.setActive(active);
        accountRepository.save(account);
    }

    public ClientAccountDto createClientAccount(ClientAccountDto clientAccountDto) throws ResourceOccupiedException {
        Optional<AbstractAccount> existingAccount = accountRepository.findByLogin(clientAccountDto.getLogin());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accountRepository.findByPersonalId(clientAccountDto.getPersonalId());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        ClientAccount clientAccount = AccountConverter.toClient(clientAccountDto);
        clientAccount.setPassword(passwordEncoder.encode(clientAccountDto.getPassword()));
        accountRepository.save(clientAccount);
        return AccountConverter.toClientDto(clientAccount);
    }

    public AdminAccountDto createAdminAccount(AdminAccountDto adminAccountDto) throws ResourceOccupiedException {
        Optional<AbstractAccount> existingAccount = accountRepository.findByLogin(adminAccountDto.getLogin());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accountRepository.findByPersonalId(adminAccountDto.getPersonalId());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        AdminAccount admin = AccountConverter.toAdmin(adminAccountDto);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        AdminAccountDto dtoAdmin = AccountConverter.toAdminDto(admin);
        accountRepository.save(admin);
        return dtoAdmin;
    }

    public ResourceManagerAccountDto createResourceManagerAccount(ResourceManagerAccountDto resourceManagerAccountDto) throws
    ResourceOccupiedException {
        Optional<AbstractAccount> existingAccount = accountRepository.findByLogin(resourceManagerAccountDto.getLogin());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given login already exists");
        }
        existingAccount = accountRepository.findByPersonalId(resourceManagerAccountDto.getPersonalId());
        if (existingAccount.isPresent()) {
            throw new ResourceOccupiedException("Account with given personal id already exists");
        }
        ResourceManagerAccount resourceManagerAccount = AccountConverter.toResourceManager(resourceManagerAccountDto);
        resourceManagerAccount.setPassword(passwordEncoder.encode(resourceManagerAccountDto.getPassword()));
        accountRepository.save(resourceManagerAccount);
        return AccountConverter.toResourceManagerDto(resourceManagerAccount);
    }
}
