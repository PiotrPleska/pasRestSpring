package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.AbstractAccountDto;
import com.example.passpringrest.dto.AdminAccountDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.dto.ResourceManagerAccountDto;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.services.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AbstractAccountDto>>  getAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAllAccounts());
    }

    @GetMapping(value = "/admins", produces = "application/json")
    public ResponseEntity<List<AbstractAccountDto>>  getAdminAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAdminAccounts());
    }

    @GetMapping(value = "/resource-managers", produces = "application/json")
    public ResponseEntity<List<AbstractAccountDto>>  getResourceManagerAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readResourceManagerAccounts());
    }

    @GetMapping(value = "/clients", produces = "application/json")
    public ResponseEntity<List<AbstractAccountDto>> getClientAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readClientAccounts());
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> getAccountById(@PathVariable("id") @NotNull String id) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAccountById(id));
    }


    @GetMapping(value = "/personal-id/{personalId}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> getAccountByPersonalId(@PathVariable("personalId") @NotNull String personalId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAccountByPersonalId(personalId));
    }

    @GetMapping(value = "/login/{login}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> getAccountByLogin(@PathVariable("login") @NotNull String login) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAccountByLogin(login));
    }

    @GetMapping(value = "/part-of-login/{partOfLogin}", produces = "application/json")
    public ResponseEntity<List<AbstractAccountDto>> getAccountsByPartOfLogin(@PathVariable("partOfLogin") @NotNull String partOfLogin) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readAccountsByPartOfLogin(partOfLogin));
    }


    @PutMapping(value = "/client/password/{login}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> updateClientAccountPasswordByLogin(@PathVariable("login") @NotNull String login,
                                                                           @RequestBody @Valid @NotNull ClientAccountDto acc) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateClientAccountPasswordByLogin(login, acc));
    }

    @PutMapping(value = "/admin/password/{login}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> updateAdminAccountPasswordByLogin(@PathVariable("login") @NotNull String login,
                                                                           @RequestBody @Valid @NotNull AdminAccountDto acc) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAdminAccountPasswordByLogin(login, acc));
    }

    @PutMapping(value = "/resource-manager/password/{login}", produces = "application/json")
    public ResponseEntity<AbstractAccountDto> updateAdminAccountPasswordByLogin(@PathVariable("login") @NotNull String login,
                                                                                @RequestBody @Valid @NotNull ResourceManagerAccountDto acc) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateResourceManagerAccountPasswordByLogin(login, acc));
    }


    @PatchMapping(value = "/activate/{login}", produces = "application/json")
    public void activateAccountByLogin(@PathVariable("login") @NotNull String login) {
        accountService.updateAccountActiveByLogin(login, true);
    }

    @PatchMapping(value = "/deactivate/{login}", produces = "application/json")
    public void deactivateAccountByLogin(@PathVariable("login") @NotNull String login) {
        accountService.updateAccountActiveByLogin(login, false);
    }


    @PostMapping(value = "/client", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ClientAccountDto> addClientAccount(@RequestBody @Valid @NotNull ClientAccountDto clientAccountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createClientAccount(clientAccountDto));
    }


    @PostMapping(value = "/admin", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AdminAccountDto> addAdminAccount(@RequestBody @Valid @NotNull AdminAccountDto adminAccountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAdminAccount(adminAccountDto));
    }


    @PostMapping(value = "/resource-manager", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResourceManagerAccountDto> addResourceManagerAccount(@RequestBody @Valid @NotNull ResourceManagerAccountDto resourceManagerAccountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createResourceManagerAccount(resourceManagerAccountDto));
    }
}
