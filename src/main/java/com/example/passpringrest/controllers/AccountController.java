package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.AdminAccountDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.dto.ResourceManagerAccountDto;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private  AccountService accountService;


    @GetMapping(produces = "application/json")
    public Response getAccounts() {
        try {
            return Response.ok(accountService.readAllAccounts()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/admins", produces = "application/json")
    public Response getAdminAccounts() {
        try {
            return Response.ok(accountService.readAdminAccounts()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/resource-managers", produces = "application/json")
    public Response getResourceManagerAccounts() {
        try {

            return Response.ok(accountService.readResourceManagerAccounts()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/clients", produces = "application/json")
    public Response getClientAccounts() {
        try {
            return Response.ok(accountService.readClientAccounts()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public Response getAccountById(@PathVariable("id") @NotNull String id) {
        try {
            return Response.ok(accountService.readAccountById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/personal-id/{personalId}", produces = "application/json")
    public Response getAccountByPersonalId(@PathVariable("personalId") @NotNull String personalId) {
        try {
            return Response.ok(accountService.readAccountByPersonalId(personalId)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/login/{login}", produces = "application/json")
    public Response getAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            return Response.ok(accountService.readAccountByLogin(login)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/part-of-login/{partOfLogin}", produces = "application/json")
    public Response getAccountsByPartOfLogin(@PathVariable("partOfLogin") @NotNull String partOfLogin) {
        try {
            return Response.ok(accountService.readAccountsByPartOfLogin(partOfLogin)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PutMapping(value = "/password/{login}", produces = "application/json")
    public Response updateAccountPasswordByLogin(@PathVariable("login") @NotNull String login, @Valid @NotNull ClientAccountDto acc) {
        try {
            accountService.updateAccountPasswordByLogin(login, acc);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PatchMapping(value = "/activate/{login}", produces = "application/json")
    public Response activateAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            accountService.updateAccountActiveByLogin(login, true);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PatchMapping(value = "/deactivate/{login}", produces = "application/json")
    public Response deactivateAccountByLogin(@PathVariable("login") @NotNull String login) {
        try {
            accountService.updateAccountActiveByLogin(login, false);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PostMapping(value = "/client/add", produces = "application/json", consumes = "application/json")
    public Response addClientAccount(@Valid @NotNull ClientAccountDto clientAccountDto) {
        try {
            accountService.createClientAccount(clientAccountDto);
            return Response.ok().build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PostMapping(value = "/admin/add", produces = "application/json", consumes = "application/json")
    public Response addAdminAccount(@Valid @NotNull AdminAccountDto adminAccountDto) {
        try {
            accountService.createAdminAccount(adminAccountDto);
            return Response.ok().build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PostMapping(value = "/resource-manager/add", produces = "application/json", consumes = "application/json")
    public Response addResourceManagerAccount(@Valid @NotNull ResourceManagerAccountDto resourceManagerAccountDto) {
        try {
            accountService.createResourceManagerAccount(resourceManagerAccountDto);
            return Response.ok().build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
