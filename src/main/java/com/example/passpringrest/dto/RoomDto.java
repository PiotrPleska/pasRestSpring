package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class RoomDto {

    @JsonProperty("id")
    private UUID id;

    @NotNull
    @Positive
    @JsonProperty("roomNumber")
    private int roomNumber;

    @NotNull
    @DecimalMin(value = "1", message = "Room capacity must be greater than 0")
    @DecimalMax(value = "5", message = "Room capacity must be less than 5")
    @JsonProperty("roomCapacity")
    private int roomCapacity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    @DecimalMax(value = "100000.0", inclusive = false, message = "Base price must be less than 100000")
    @JsonProperty("basePrice")
    private double basePrice;

    @JsonProperty("isRented")
    private int isRented;

    public RoomDto() {
    }

    @JsonCreator
    public RoomDto(@JsonProperty("roomNumber") int roomNumber, @JsonProperty("roomCapacity") int roomCapacity,
                   @JsonProperty("basePrice") double basePrice) {
        this.id = UUID.randomUUID();
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }

    public RoomDto(UUID id, int roomNumber, int roomCapacity, double basePrice, int isRented) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
        this.isRented = isRented;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getIsRented() {
        return isRented;
    }

    public void setIsRented(int isRented) {
        this.isRented = isRented;
    }
}
