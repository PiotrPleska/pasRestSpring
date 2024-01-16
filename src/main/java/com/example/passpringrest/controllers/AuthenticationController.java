package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.*;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/client")
    public ResponseEntity<String> registerClient(
            @RequestBody ClientAccountDto request
    ) {
        return ResponseEntity.ok(service.registerClient(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(
            @RequestBody AdminAccountDto request
    ) {
        return ResponseEntity.ok(service.registerAdmin(request));
    }

    @PostMapping("/resource-manager")
    public ResponseEntity<String> registerResourceManager(
            @RequestBody ResourceManagerAccountDto request
    ) {
        return ResponseEntity.ok(service.registerResourceManager(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @RequestBody AuthenticationDto request,
            HttpServletResponse response
    ) {
        String token = service.authenticate(request);

        Cookie cookie = new Cookie("token", token);

        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok("Authentication successful");
    }
}