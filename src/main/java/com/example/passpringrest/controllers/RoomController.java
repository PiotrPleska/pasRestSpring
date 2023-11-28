package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;



    @GetMapping(produces = "application/json")
    public Response getRooms() {
        try {
            return Response.ok(roomService.readAllRooms()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Response getRoomById(@PathVariable @NotNull String id) {
        try {
            return Response.ok(roomService.readRoomById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GetMapping(value = "/room-number/{roomNumber}", produces = "application/json")
    public Response getRoomByRoomNumber(@PathVariable("roomNumber") @NotNull int roomNumber) {
        try {
            return Response.ok(roomService.readRoomByRoomNumber(roomNumber)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PutMapping(value = "/price/{roomNumber}", produces = "application/json")
    public Response updateRoomPrice(@PathVariable("roomNumber") @NotNull int roomNumber, @Valid @NotNull RoomDto roomDto) {
        try {
            roomService.updateRoomPrice(roomNumber, roomDto.getBasePrice());
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PutMapping(value = "/capacity/{roomNumber}", produces = "application/json")
    public Response updateRoomCapacity(@PathVariable("roomNumber") @NotNull int roomNumber, @Valid @NotNull RoomDto roomDto) {
        try {
            roomService.updateRoomCapacity(roomNumber, roomDto.getRoomCapacity());
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DeleteMapping(value = "/{roomNumber}", produces = "application/json")
    public Response deleteRoom(@PathVariable("roomNumber") @NotNull int roomNumber) {
        try {
            roomService.deleteRoom(roomNumber);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @PostMapping(value = "/add",produces = "application/json")
    public Response addRoom(@Valid @NotNull RoomDto roomDto) {
        try {
            roomService.createRoom(roomDto);
            return Response.ok().build();
        } catch (ResourceOccupiedException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


}
