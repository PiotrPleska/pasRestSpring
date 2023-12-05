package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.GregorianCalendar;

public class RentDtoPost {
    private GregorianCalendar rentStartDate;
    private String accountId;
    private String roomId;

    public RentDtoPost() {
    }

    @JsonCreator
    public RentDtoPost(@JsonProperty("rentStartDate") GregorianCalendar rentStartDate, @JsonProperty("accountId") String accountId, @JsonProperty(
            "roomId") String roomId) {
        this.rentStartDate = rentStartDate;
        this.accountId = accountId;
        this.roomId = roomId;
    }

    public GregorianCalendar getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(GregorianCalendar rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
