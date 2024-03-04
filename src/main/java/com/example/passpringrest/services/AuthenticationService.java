package com.example.passpringrest.services;

import com.example.passpringrest.dto.*;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.repositories.AccountRepository;
import com.example.passpringrest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String registerClient(ClientAccountDto request) {
        ClientAccountDto user = new ClientAccountDto(request.getLogin(), request.getPassword(), request.getPersonalId());
        accountService.createClientAccount(user);
        return jwtService.generateToken(user);
    }

    public String registerAdmin(AdminAccountDto request) {
        AdminAccountDto user = new AdminAccountDto(request.getLogin(), request.getPassword(), request.getPersonalId());
        accountService.createAdminAccount(user);
        return jwtService.generateToken(user);
    }

    public String registerResourceManager(ResourceManagerAccountDto request) {
        ResourceManagerAccountDto user = new ResourceManagerAccountDto(request.getLogin(), request.getPassword(), request.getPersonalId());
        accountService.createResourceManagerAccount(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(AuthenticationDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = accountRepository.findByLogin(request.getLogin()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return jwtService.generateToken(user);
    }
}
