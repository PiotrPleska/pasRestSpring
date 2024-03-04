package com.example.passpringrest.services;

import com.example.passpringrest.converters.RentConverter;
import com.example.passpringrest.dto.RentDtoGet;
import com.example.passpringrest.dto.RentDtoPost;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.Rent;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.example.passpringrest.repositories.AccountRepository;
import com.example.passpringrest.repositories.RentRepository;
import com.example.passpringrest.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;


    private final RoomRepository roomRepository;


    private final AccountRepository accountRepository;

    public List<RentDtoGet> readAllRents() throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findAll();
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllRentsByAccountId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findByClientAccount_Id(UUID.fromString(id));
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllCurrentRentsByAccountId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findCurrentRentsByClientId(id);
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllPastRentsByAccountId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findPastRentsByClientId(id);
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllRentsByRoomId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findAllByRoom_Id(UUID.fromString(id));
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllCurrentRents() throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findByRentEndDateIsNull();
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllPastRents() throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findByRentEndDateIsNotNull();
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllCurrentRentsByRoomId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findCurrentRentsByRoomId(id);
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public List<RentDtoGet> readAllPastRentsByRoomId(String id) throws ResourceNotFoundException {
        List<Rent> rents = rentRepository.findPastRentsByRoomId(id);
        List<RentDtoGet> rentDtoGets = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtoGets.add(RentConverter.toDto(rent));
        }
        if (rentDtoGets.isEmpty())
            throw new ResourceNotFoundException("Rents not found");
        return rentDtoGets;
    }

    public RentDtoGet readRentById(String id) throws ResourceNotFoundException {
        return RentConverter.toDto(rentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Rent with given id" +
                " not found")));
    }

    public void deleteRent(String id) throws ResourceNotFoundException, ResourceOccupiedException {
        Rent rent = rentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Rent with given id not found"));
        if (rent.getRentEndDate() != null) {
            throw new ResourceOccupiedException("Rent is already finished");
        }
        rent.setRentEndDate(new GregorianCalendar());
        rentRepository.save(rent);
    }

    public RentDtoGet createRent(RentDtoPost rentDtoPost) throws ResourceNotFoundException {
        AbstractAccount account =
                accountRepository.findById(UUID.fromString(rentDtoPost.getAccountId())).orElseThrow(() -> new ResourceNotFoundException("Account " +
                        "does not exist"));
        Room room = roomRepository.findById(UUID.fromString(rentDtoPost.getRoomId())).orElseThrow(() -> new ResourceNotFoundException("Room does " +
                "not exist"));
        Rent rent = new Rent(rentDtoPost.getRentStartDate(), (ClientAccount) account, room);
        rentRepository.save(rent);
        return RentConverter.toDto(rent);
    }

}
