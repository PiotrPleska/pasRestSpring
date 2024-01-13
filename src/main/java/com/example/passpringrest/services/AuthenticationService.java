package com.example.passpringrest.services;

import com.example.passpringrest.dto.AuthenticationDto;
import com.example.passpringrest.dto.AuthenticationResponseDto;
import com.example.passpringrest.dto.RegisterDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountService accountService;
    //widze kiwke, ze zaraz uzywania serwisu uzywamy repo,
    // tylko ze wtedy nie dto musza implementowac UserDetails tylko zwykle klasy
    // ale no to jest giga proste do zmiany, potrzebuje tylko potwierdzenia czy to jest ok
    // bo w sumie my nigdzie w endpointach nie dajemy mozliwosci do pobrania hasla,
    // bo tylko kiwka tutaj jest robiona
    // a nawet jesli to haslo jest zahashowane

    //jezeli to jest waszym zdaniem ok to wtedy dziala na milion procent wszystko pog?
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterDto request) {
        ClientAccountDto user = new ClientAccountDto(request.getLogin(), passwordEncoder.encode(request.getPassword()), request.getPersonalId());
        accountService.createClientAccount(user);
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
        var user = accountService.readAccountByLogin(request.getLogin());
        System.out.println(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(token)
                .build();
    }
}
