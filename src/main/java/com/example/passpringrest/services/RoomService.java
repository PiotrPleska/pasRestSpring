package com.example.passpringrest.services;

import com.example.passpringrest.converters.RoomConverter;
import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;


    public List<RoomDto> readAllRooms() throws ResourceNotFoundException {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            throw new ResourceNotFoundException("No rooms found");
        }
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            roomDtos.add(RoomConverter.toDto(room));
        }
        return roomDtos;
    }

    public RoomDto readRoomById(String id) throws ResourceNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            return RoomConverter.toDto(roomRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Room with given id not found")));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public RoomDto readRoomByRoomNumber(int roomNumber) throws ResourceNotFoundException {
        try {
            Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new ResourceNotFoundException("Room with given room number not found"));
            return RoomConverter.toDto(room);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    @Transactional
    public RoomDto createRoom(RoomDto roomDto) {
        Optional<Room> existingRoom = roomRepository.findByRoomNumber(roomDto.getRoomNumber());
        if (existingRoom.isEmpty()) {
            Room room = RoomConverter.toEntity(roomDto);
            roomRepository.save(room);
            return RoomConverter.toDto(room);
        }
        throw new ResourceOccupiedException("Room with given room number already exists");
    }

    @Transactional
    public RoomDto updateRoomPrice(int roomNumber, double newPrice) throws ResourceNotFoundException {
        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new ResourceNotFoundException("No room with given room number"));
        room.setBasePrice(newPrice);
        roomRepository.save(room);
        return RoomConverter.toDto(room);
    }

    @Transactional
    public RoomDto updateRoomCapacity(int roomNumber, int newCapacity) throws ResourceNotFoundException {
        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new ResourceNotFoundException("No room with given room number"));
        room.setRoomCapacity(newCapacity);
        roomRepository.save(room);
        return RoomConverter.toDto(room);
    }

    @Transactional
    public void deleteRoom(int roomNumber) throws ResourceNotFoundException, ResourceOccupiedException {
        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new ResourceNotFoundException("No room with given room number"));
        if (room.isRented()) {
            throw new ResourceOccupiedException("Room is rented");
        }
        roomRepository.delete(room);
    }
}
