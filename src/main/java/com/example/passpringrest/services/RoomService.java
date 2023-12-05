package com.example.passpringrest.services;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.converters.RoomConverter;
import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;


    public List<RoomDto> readAllRooms() throws ResourceNotFoundException {
        List<Room> rooms = roomRepository.readAllRooms();
        if(rooms.isEmpty()) {
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
            MongoUUID mongoUUID = new MongoUUID(uuid);
            return RoomConverter.toDto(roomRepository.readRoomById(mongoUUID));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Invalid id");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public RoomDto readRoomByRoomNumber(int roomNumber) throws ResourceNotFoundException {
        try {
            Room room = roomRepository.readRoomByRoomNumber(roomNumber);
            return RoomConverter.toDto(room);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public RoomDto createRoom(RoomDto roomDto) {
        Room existingRoom = roomRepository.readRoomByRoomNumberWithoutException(roomDto.getRoomNumber());
        if (existingRoom == null) {
            Room room = RoomConverter.toEntity(roomDto);
            roomRepository.insertRoom(room);
            return RoomConverter.toDto(room);
        }
        throw new ResourceOccupiedException("Room with given room number already exists");
    }

    public RoomDto updateRoomPrice(int roomNumber, double newPrice) throws ResourceNotFoundException {
        try {
            readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        roomRepository.updateRoomPrice(roomNumber, newPrice);
        Room room = roomRepository.readRoomByRoomNumber(roomNumber);
        return RoomConverter.toDto(room);
    }

    public RoomDto updateRoomCapacity(int roomNumber, int newCapacity) throws ResourceNotFoundException {
        try {
            readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        roomRepository.updateRoomCapacity(roomNumber, newCapacity);
        Room room = roomRepository.readRoomByRoomNumber(roomNumber);
        return RoomConverter.toDto(room);
    }

    public void deleteRoom(int roomNumber) throws ResourceNotFoundException, ResourceOccupiedException {
        Room room = roomRepository.readRoomByRoomNumber(roomNumber);
        if (room == null) {
            throw new ResourceNotFoundException("Room does not exist");
        }
        if (room.getIsRented() > 0) {
            throw new ResourceOccupiedException("Room is rented");
        }
        roomRepository.deleteRoom(roomNumber);
    }
}
