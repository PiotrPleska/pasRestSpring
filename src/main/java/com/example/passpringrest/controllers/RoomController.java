package com.example.passpringrest.controllers;

import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.services.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.readAllRooms());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable @NotNull String id) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.readRoomById(id));
    }

    @GetMapping(value = "/room-number/{roomNumber}", produces = "application/json")
    public ResponseEntity<RoomDto> getRoomByRoomNumber(@PathVariable("roomNumber") @NotNull int roomNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.readRoomByRoomNumber(roomNumber));
    }

    @PutMapping(value = "/price/{roomNumber}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<RoomDto> updateRoomPrice(@PathVariable("roomNumber") int roomNumber, @RequestBody @Valid @NotNull RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoomPrice(roomNumber, roomDto.getBasePrice()));
    }

    @PutMapping(value = "/capacity/{roomNumber}", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<RoomDto> updateRoomCapacity(@PathVariable("roomNumber") @NotNull int roomNumber,
                                                      @RequestBody @Valid @NotNull RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoomCapacity(roomNumber, roomDto.getRoomCapacity()));
    }

    @DeleteMapping(value = "/{roomNumber}", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public void deleteRoom(@PathVariable("roomNumber") @NotNull int roomNumber) {
        roomService.deleteRoom(roomNumber);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_RESOURCE_MANAGER')")
    public ResponseEntity<RoomDto> addRoom(@RequestBody @Valid @NotNull RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(roomDto));
    }

}
