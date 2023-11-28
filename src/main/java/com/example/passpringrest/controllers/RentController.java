package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.DateDto;
import com.example.passpringrest.dto.RentDtoGet;
import com.example.passpringrest.dto.RentDtoPost;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.RentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("api/rents")
@RequiredArgsConstructor
public class RentController {


    private final RentService rentService;


    @GetMapping(produces = "application/json")
    public List<RentDtoGet> getRents() {
        try {
            return rentService.readAllRents();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public RentDtoGet getRentById(@PathVariable("id") String id) {
        try {
            return rentService.readRentById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/current", produces = "application/json")
    public List<RentDtoGet> getCurrentRents() {
        try {
            return rentService.readAllCurrentRents();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/past", produces = "application/json")
    public List<RentDtoGet> getPastRents() {
        try {
            return rentService.readAllPastRents();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/current/room-id/{id}", produces = "application/json")
    public List<RentDtoGet> getCurrentRentsByRoomId(@PathVariable("id") String id) {
        try {
            return rentService.readAllCurrentRentsByRoomId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @GetMapping(value = "/past/room-id/{id}", produces = "application/json")
    public List<RentDtoGet> getPastRentsByRoomId(@PathVariable("id") String id) {
        try {
            return rentService.readAllPastRentsByRoomId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/current/account-id/{id}", produces = "application/json")
    public List<RentDtoGet> getCurrentRentsByAccountId(@PathVariable("id") String id) {
        try {

            return rentService.readAllCurrentRentsByAccountId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/past/account-id/{id}", produces = "application/json")
    public List<RentDtoGet> getPastRentsByAccountId(@PathVariable("id") String id) {
        try {
            return rentService.readAllPastRentsByAccountId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/account-id/{id}", produces = "application/json")
    public List<RentDtoGet> getRentsByAccountId(@PathVariable("id") String id) {
        try {
            return rentService.readAllRentsByAccountId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/room-id/{id}", produces = "application/json")
    public List<RentDtoGet> getRentsByRoomId(@PathVariable("id") String id) {
        try {
            return rentService.readAllRentsByRoomId(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void deleteRent(@PathVariable("id") String id) {
        try {
            rentService.deleteRent(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


//    @PatchMapping(value = "/endRent/{id}", produces = "application/json")
//    public void updateEndRentDate(@PathVariable("id") String id, @Valid DateDto dateDto) {
//        try {
//            // TODO napisać funkcję która przyjmie id i datę zakończenia renta i zakończy renta
//            System.out.println(dateDto.getDate().toZonedDateTime());
//            return Response.ok().build();
//        } catch (ResourceNotFoundException e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
//        } catch (ResourceOccupiedException e) {
//            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }


    @PostMapping(value = "/add", produces = "application/json")
    public void addRent(@RequestBody @Valid @NotNull RentDtoPost rentDtoPost) {
        try {
            rentService.createRent(rentDtoPost);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
