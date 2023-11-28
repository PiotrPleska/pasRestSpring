package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.AbstractAccountDto;
import com.example.passpringrest.dto.AdminAccountDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.dto.ResourceManagerAccountDto;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping(produces = "application/json")
    public List<AbstractAccountDto> getAccounts() {
        try {
            return accountService.readAllAccounts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/admins", produces = "application/json")
    public List<AbstractAccountDto> getAdminAccounts() {
        try {
            return accountService.readAdminAccounts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/resource-managers", produces = "application/json")
    public List<AbstractAccountDto> getResourceManagerAccounts() {
        try {

            return accountService.readResourceManagerAccounts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/clients", produces = "application/json")
    public List<AbstractAccountDto> getClientAccounts() {
        try {
            return accountService.readClientAccounts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public AbstractAccountDto getAccountById(@PathVariable("id") @NotNull String id) {
        try {
            return accountService.readAccountById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @GetMapping(value = "/personal-id/{personalId}", produces = "application/json")
    public AbstractAccountDto getAccountByPersonalId(@PathVariable("personalId") @NotNull String personalId) {
        try {
            return accountService.readAccountByPersonalId(personalId);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/login/{login}", produces = "application/json")
    public AbstractAccountDto getAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            return accountService.readAccountByLogin(login);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/part-of-login/{partOfLogin}", produces = "application/json")
    public List<AbstractAccountDto> getAccountsByPartOfLogin(@PathVariable("partOfLogin") @NotNull String partOfLogin) {
        try {
            return accountService.readAccountsByPartOfLogin(partOfLogin);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PutMapping(value = "/password/{login}", produces = "application/json")
    public void updateAccountPasswordByLogin(@PathVariable("login") @NotNull String login,@RequestBody @Valid @NotNull ClientAccountDto acc) {
        try {
            accountService.updateAccountPasswordByLogin(login, acc);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PatchMapping(value = "/activate/{login}", produces = "application/json")
    public void activateAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            accountService.updateAccountActiveByLogin(login, true);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping(value = "/deactivate/{login}", produces = "application/json")
    public void deactivateAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            accountService.updateAccountActiveByLogin(login, false);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping(value = "/client", produces = "application/json", consumes = "application/json")
    public void addClientAccount(@RequestBody @Valid @NotNull ClientAccountDto clientAccountDto) {
        try {
            accountService.createClientAccount(clientAccountDto);
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping(value = "/admin", produces = "application/json", consumes = "application/json")
    public void addAdminAccount(@RequestBody @Valid @NotNull AdminAccountDto adminAccountDto) {
        try {
            accountService.createAdminAccount(adminAccountDto);
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping(value = "/resource-manager", produces = "application/json", consumes = "application/json")
    public void addResourceManagerAccount(@RequestBody @Valid @NotNull ResourceManagerAccountDto resourceManagerAccountDto) {
        try {
            accountService.createResourceManagerAccount(resourceManagerAccountDto);
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
