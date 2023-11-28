package com.example.passpringrest.entities;

import com.example.passpringrest.codecs.MongoUUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.GregorianCalendar;

public class Rent {

    @BsonProperty("_id")
    private MongoUUID rentId;

    @BsonProperty("rentStartDate")
    private GregorianCalendar rentStartDate;

    @BsonProperty("rentEndDate")
    private GregorianCalendar rentEndDate;

    @BsonProperty("account")
    private ClientAccount clientAccount;

    @BsonProperty("room")
    private Room room;

    public void setClientAccount(ClientAccount clientAccount) {
        this.clientAccount = clientAccount;
    }

    public Rent(MongoUUID uuid, GregorianCalendar rentStartDate, ClientAccount clientAccount, Room room) {
        this.rentId = uuid;
        this.rentStartDate = rentStartDate;
        this.clientAccount = clientAccount;
        this.room = room;
    }

    @BsonCreator
    public Rent(@BsonProperty("_id") MongoUUID rentId, @BsonProperty("rentStartDate") GregorianCalendar rentStartDate,
                @BsonProperty("rentEndDate") GregorianCalendar rentEndDate, @BsonProperty("account") ClientAccount clientAccount, @BsonProperty(
                        "room") Room room) {
        this.rentId = rentId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.clientAccount = clientAccount;
        this.room = room;
    }

    public Rent() {
    }

    public MongoUUID getRentId() {
        return rentId;
    }

    public void setRentId(MongoUUID rentId) {
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

    public ClientAccount getClientAccount() {
        return clientAccount;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void endRent(GregorianCalendar endDate) {
        if (endDate.before(rentStartDate)) {
            rentEndDate = rentStartDate;
        } else {
            rentEndDate = endDate;
        }
    }


    @Override
    public String toString() {
        return "Rent{" + "rentId=" + rentId +
//                ", rentStartDate=" + rentStartDate.toString() +
                ", client=" + clientAccount + ", room=" + room + '}';
    }

}

