package com.example.passpringrest.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Access(AccessType.FIELD)
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "room_number", nullable = false)
    private int roomNumber;

    @Column(name = "room_capacity", nullable = false)
    private int roomCapacity;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Version
    private long version;

    @Column(name = "is_rented", nullable = false)
    private boolean isRented;

    public Room(UUID uuid, int roomNumber, int roomCapacity, double basePrice) {
        this.id = uuid;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }


    public String toString() {
        return "Room{" + "id=" + id + ", roomNumber=" + roomNumber + ", roomCapacity=" + roomCapacity + ", basePrice=" + basePrice + '}';
    }
}
