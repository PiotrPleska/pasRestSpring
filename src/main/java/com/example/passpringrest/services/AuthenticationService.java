package com.example.passpringrest.services;

import com.example.passpringrest.dto.AuthenticationDto;
import com.example.passpringrest.dto.AuthenticationResponseDto;
import com.example.passpringrest.dto.RegisterDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.ClientAccount;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterDto request) {
        AbstractAccount user = new ClientAccount(request.getLogin(), passwordEncoder.encode(request.getPassword()), request.getPersonalId(),true);
        accountRepository.insertAccount(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponseDto authenticate(AuthenticationDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = accountRepository.readAccountByLogin(request.getLogin());
        var token = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }
}
