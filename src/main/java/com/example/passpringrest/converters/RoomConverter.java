package com.example.passpringrest.converters;


import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.entities.Room;

public class RoomConverter {

    public static Room toEntity(RoomDto dto) {
        return new Room(dto.getId(), dto.getRoomNumber(), dto.getRoomCapacity(), dto.getBasePrice());
    }

    public static RoomDto toDto(Room entity) {
        return new RoomDto(entity.getId(), entity.getRoomNumber(), entity.getRoomCapacity(), entity.getBasePrice(), entity.isRented());
    }
}
