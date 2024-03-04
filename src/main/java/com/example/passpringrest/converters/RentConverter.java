package com.example.passpringrest.converters;


import com.example.passpringrest.dto.RentDtoGet;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.Rent;

public class RentConverter {

    public static Rent toEntity(RentDtoGet dto) {
        return new Rent(dto.getRentId(), dto.getRentStartDate(), AccountConverter.toClient(dto.getAccount()),
                RoomConverter.toEntity(dto.getRoom()));
    }

    public static RentDtoGet toDto(Rent entity) {
        return new RentDtoGet(entity.getRentId(), entity.getRentStartDate(), entity.getRentEndDate(),
                AccountConverter.toClientDto((ClientAccount) entity.getClientAccount()), RoomConverter.toDto(entity.getRoom()));
    }
}
