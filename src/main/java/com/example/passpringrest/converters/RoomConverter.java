package com.example.passpringrest.converters;


import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.dto.RoomDto;
import com.example.passpringrest.entities.Room;

public class RoomConverter {

    public static Room toEntity(RoomDto dto) {
        return new Room(new MongoUUID(dto.getId()), dto.getRoomNumber(), dto.getRoomCapacity(), dto.getBasePrice());
    }

    public static RoomDto toDto(Room entity) {
        return new RoomDto(entity.getId().getUuid(), entity.getRoomNumber(), entity.getRoomCapacity(), entity.getBasePrice(), entity.getIsRented());
    }
}
