package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.services.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;



    @GetMapping(produces = "application/json")
    public List<RoomDto> getRooms() {
        try {
            return roomService.readAllRooms();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public RoomDto getRoomById(@PathVariable @NotNull String id) {
        try {
            return roomService.readRoomById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/room-number/{roomNumber}", produces = "application/json")
    public RoomDto getRoomByRoomNumber(@PathVariable("roomNumber") @NotNull int roomNumber) {
        try {
            return roomService.readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/price/{roomNumber}", consumes = "application/json", produces = "application/json")
    public void updateRoomPrice(@PathVariable("roomNumber") int roomNumber,@RequestBody @Valid @NotNull RoomDto roomDto) {
        try {
            roomService.updateRoomPrice(roomNumber, roomDto.getBasePrice());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/capacity/{roomNumber}", produces = "application/json")
    public void updateRoomCapacity(@PathVariable("roomNumber") @NotNull int roomNumber,@RequestBody @Valid @NotNull RoomDto roomDto) {
        try {
            roomService.updateRoomCapacity(roomNumber, roomDto.getRoomCapacity());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(value = "/{roomNumber}", produces = "application/json")
    public void deleteRoom(@PathVariable("roomNumber") @NotNull int roomNumber) {
        try {
            roomService.deleteRoom(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public void addRoom(@RequestBody @Valid @NotNull RoomDto roomDto) {
        try {
            roomService.createRoom(roomDto);
        } catch (ResourceOccupiedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
