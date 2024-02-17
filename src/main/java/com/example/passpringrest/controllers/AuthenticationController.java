package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.*;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.services.AuthenticationService;
import com.example.passpringrest.services.SecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final SecurityService securityService;

    @PostMapping("/client")
    public ResponseEntity<String> registerClient(
            @RequestBody ClientAccountDto request
    ) {
        return ResponseEntity.ok(service.registerClient(request));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(
            @RequestBody AdminAccountDto request
    ) {
        return ResponseEntity.ok(service.registerAdmin(request));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/resource-manager")
    public ResponseEntity<String> registerResourceManager(
            @RequestBody ResourceManagerAccountDto request
    ) {
        return ResponseEntity.ok(service.registerResourceManager(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDto request) {
        String etag = securityService.generateEtag(request.getLogin());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, etag).body(service.authenticate(request));
    }
}