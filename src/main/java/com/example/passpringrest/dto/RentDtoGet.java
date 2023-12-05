package com.example.passpringrest.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.GregorianCalendar;
import java.util.UUID;

public class RentDtoGet {

    @JsonProperty("id")
    private UUID rentId;

    @JsonProperty("rentStartDate")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private GregorianCalendar rentStartDate;

    @JsonProperty("rentEndDate")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private GregorianCalendar rentEndDate;

    @JsonProperty("account")
    private ClientAccountDto account;

    @JsonProperty("room")
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
