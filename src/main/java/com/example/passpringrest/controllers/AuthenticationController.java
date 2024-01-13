package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.AuthenticationDto;
import com.example.passpringrest.dto.AuthenticationResponseDto;
import com.example.passpringrest.dto.RegisterDto;
import com.example.passpringrest.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody RegisterDto request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody AuthenticationDto request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}