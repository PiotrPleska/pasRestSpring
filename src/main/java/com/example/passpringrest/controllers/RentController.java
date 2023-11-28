package com.example.passpringrest.controllers;


import com.example.passpringrest.dto.DateDto;
import com.example.passpringrest.dto.RentDtoPost;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.ws.rs.core.Response;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/rents")
public class RentController {


    private  RentService rentService;




    @GetMapping(produces = "application/json")
    public Response getRents() {
        try {
            return Response.ok(rentService.readAllRents()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public Response getRentById(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readRentById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/current", produces = "application/json")
    public Response getCurrentRents() {
        try {
            return Response.ok(rentService.readAllCurrentRents()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/past", produces = "application/json")
    public Response getPastRents() {
        try {
            return Response.ok(rentService.readAllPastRents()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/current/room-id/{id}", produces = "application/json")
    public Response getCurrentRentsByRoomId(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readAllCurrentRentsByRoomId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GetMapping(value = "/past/room-id/{id}", produces = "application/json")
    public Response getPastRentsByRoomId(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readAllPastRentsByRoomId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/current/account-id/{id}", produces = "application/json")
    public Response getCurrentRentsByAccountId(@PathVariable("id") String id) {
        try {

            return Response.ok(rentService.readAllCurrentRentsByAccountId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/past/account-id/{id}", produces = "application/json")
    public Response getPastRentsByAccountId(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readAllPastRentsByAccountId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/account-id/{id}", produces = "application/json")
    public Response getRentsByAccountId(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readAllRentsByAccountId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GetMapping(value = "/room-id/{id}", produces = "application/json")
    public Response getRentsByRoomId(@PathVariable("id") String id) {
        try {
            return Response.ok(rentService.readAllRentsByRoomId(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Response deleteRent(@PathVariable("id") String id) {
        try {
            rentService.deleteRent(id);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PatchMapping(value = "/endRent/{id}", produces = "application/json")
    public Response updateEndRentDate(@PathVariable("id") String id, @Valid DateDto dateDto) {
        try {
            // TODO napisać funkcję która przyjmie id i datę zakończenia renta i zakończy renta
            System.out.println(dateDto.getDate().toZonedDateTime());
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @PostMapping(value = "/add", produces = "application/json")
    public Response addRent(@Valid RentDtoPost rentDtoPost) {
        try {
            rentService.createRent(rentDtoPost);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


}
