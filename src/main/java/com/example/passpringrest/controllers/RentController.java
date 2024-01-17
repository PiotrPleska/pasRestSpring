package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.RentDtoGet;
import com.example.passpringrest.dto.RentDtoPost;
import com.example.passpringrest.services.SecurityService;
import com.example.passpringrest.services.RentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/rents")
@RequiredArgsConstructor
public class RentController {


    private final RentService rentService;

    private final SecurityService securityService;


    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllRents());
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("@securityService.canAccessRentByRentId(authentication, #id) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<RentDtoGet> getRentById(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readRentById(id));
    }


    @GetMapping(value = "/current", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getCurrentRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllCurrentRents());
    }


    @GetMapping(value = "/past", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getPastRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllPastRents());
    }


    @GetMapping(value = "/current/room-id/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getCurrentRentsByRoomId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllCurrentRentsByRoomId(id));
    }


    @GetMapping(value = "/past/room-id/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getPastRentsByRoomId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllPastRentsByRoomId(id));
    }


    @GetMapping(value = "/current/account-id/{id}", produces = "application/json")
    @PreAuthorize("@securityService.canAccessByAccountId(authentication, #id) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getCurrentRentsByAccountId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllCurrentRentsByAccountId(id));
    }


    @GetMapping(value = "/past/account-id/{id}", produces = "application/json")
    @PreAuthorize("@securityService.canAccessByAccountId(authentication, #id) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getPastRentsByAccountId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllPastRentsByAccountId(id));
    }


    @GetMapping(value = "/account-id/{id}", produces = "application/json")
    @PreAuthorize("@securityService.canAccessByAccountId(authentication, #id) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getRentsByAccountId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllRentsByAccountId(id));
    }


    @GetMapping(value = "/room-id/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<List<RentDtoGet>> getRentsByRoomId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.readAllRentsByRoomId(id));
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("@securityService.canAccessRentByRentId(authentication, #id) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public void deleteRent(@PathVariable("id") String id) {
        rentService.deleteRent(id);
    }


    @PostMapping(produces = "application/json")
    @PreAuthorize("@securityService.canAccessByAccountId(authentication, #rentDtoPost.accountId) or hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<RentDtoGet> addRent(@RequestBody @Valid @NotNull RentDtoPost rentDtoPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.createRent(rentDtoPost));
    }


}
