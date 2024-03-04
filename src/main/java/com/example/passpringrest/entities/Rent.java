package com.example.passpringrest.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.GregorianCalendar;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rent_id", updatable = false, nullable = false)
    private UUID rentId;

    @Column(name = "rent_start_date", nullable = false)
    private GregorianCalendar rentStartDate;

    @Column(name = "rent_end_date", nullable = false)
    private GregorianCalendar rentEndDate;

    @ManyToOne
    @JoinColumn
    private AbstractAccount clientAccount;


    @ManyToOne
    @JoinColumn
    private Room room;

    public Rent(GregorianCalendar rentStartDate, ClientAccount account, Room room) {
        this.rentStartDate = rentStartDate;
        this.clientAccount = account;
        this.room = room;
    }

    public Rent(UUID rentId, GregorianCalendar rentStartDate, ClientAccount client, Room entity) {
        this.rentId = rentId;
        this.rentStartDate = rentStartDate;
        this.clientAccount = client;
        this.room = entity;
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

