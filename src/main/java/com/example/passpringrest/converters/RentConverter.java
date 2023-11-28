package com.example.passpringrest.converters;


import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.dto.RentDtoGet;
import com.example.passpringrest.entities.Rent;

public class RentConverter {

    public static Rent toEntity(RentDtoGet dto) {
        return new Rent(new MongoUUID(dto.getRentId()), dto.getRentStartDate(), AccountConverter.toClient(dto.getAccount()),
                RoomConverter.toEntity(dto.getRoom()));
    }

    public static RentDtoGet toDto(Rent entity) {
        return new RentDtoGet(entity.getRentId().getUuid(), entity.getRentStartDate(), entity.getRentEndDate(),
                AccountConverter.toClientDto(entity.getClientAccount()), RoomConverter.toDto(entity.getRoom()));
    }
}
