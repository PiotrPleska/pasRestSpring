package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
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
    private boolean isRented;

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

    public RoomDto(UUID id, int roomNumber, int roomCapacity, double basePrice, boolean isRented) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
        this.isRented = isRented;
    }

}
