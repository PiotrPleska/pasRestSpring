package com.example.passpringrest.dto;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.GregorianCalendar;
import java.util.UUID;

public class RentDtoGet {

    @JsonbProperty("id")
    private UUID rentId;

    @JsonbProperty("rentStartDate")
    private GregorianCalendar rentStartDate;

    @JsonbProperty("rentEndDate")
    private GregorianCalendar rentEndDate;

    @JsonbProperty("account")
    private ClientAccountDto account;

    @JsonbProperty("room")
    private RoomDto room;

    public RentDtoGet() {
    }

    public RentDtoGet(UUID rentId, GregorianCalendar rentStartDate, GregorianCalendar rentEndDate, ClientAccountDto account, RoomDto room) {
        this.rentId = rentId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.account = account;
        this.room = room;
    }

    public UUID getRentId() {
        return rentId;
    }

    public void setRentId(UUID rentId) {
        this.rentId = rentId;
    }

    public GregorianCalendar getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(GregorianCalendar rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public GregorianCalendar getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(GregorianCalendar rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public ClientAccountDto getAccount() {
        return account;
    }

    public void setAccount(ClientAccountDto account) {
        this.account = account;
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }
}
