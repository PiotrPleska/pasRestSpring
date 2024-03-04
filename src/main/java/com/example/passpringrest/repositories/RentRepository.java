package com.example.passpringrest.repositories;

import com.example.passpringrest.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RentRepository extends JpaRepository<Rent, UUID> {

    List<Rent> findByClientAccount_Id(UUID id);

    @Query("SELECT r FROM Rent r WHERE r.clientAccount.id = :id AND r.rentEndDate is null")
    List<Rent> findCurrentRentsByClientId(@Param("id") String id);

    @Query("SELECT r FROM Rent r WHERE r.clientAccount.id = :id AND r.rentEndDate is not null")
    List<Rent> findPastRentsByClientId(@Param("id") String id);

    List<Rent> findByRentEndDateIsNull();

    List<Rent> findByRentEndDateIsNotNull();

    @Query("SELECT r FROM Rent r WHERE r.room.id = :id AND r.rentEndDate is null")
    List<Rent> findCurrentRentsByRoomId(@Param("id") String id);

    @Query("SELECT r FROM Rent r WHERE r.room.id = :id AND r.rentEndDate is not null")
    List<Rent> findPastRentsByRoomId(@Param("id") String id);

    List<Rent> findAllByRoom_Id(UUID id);
}
