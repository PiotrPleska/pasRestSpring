package com.example.passpringrest.services;

import com.example.passpringrest.codecs.MongoUUID;
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
        try {
            List<Rent> rents = rentRepository.readAllRents();
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readRentsByAccountId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllCurrentRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllCurrentRentsByAccountId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllPastRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllPastRentsByAccountId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readRentsByRoomId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllCurrentRents() throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllCurrentRents();
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllPastRents() throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllPastRents();
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllCurrentRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllCurrentRentsByRoomId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public List<RentDtoGet> readAllPastRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentRepository.readAllPastRentsByRoomId(id);
            List<RentDtoGet> rentDtoGets = new ArrayList<>();
            for (Rent rent : rents) {
                rentDtoGets.add(RentConverter.toDto(rent));
            }
            if(rentDtoGets.isEmpty())
                throw new ResourceNotFoundException("Rents not found");
            return rentDtoGets;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rents not found");
        }
    }

    public RentDtoGet readRentById(String id) throws ResourceNotFoundException {
        try {
            return RentConverter.toDto(rentRepository.readRentById(new MongoUUID(UUID.fromString(id))));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Rent not found");
        }
    }

    public void deleteRent(String id) throws ResourceNotFoundException, ResourceOccupiedException {
        Rent rent = rentRepository.readRentById(new MongoUUID(UUID.fromString(id)));
        if (rent == null) {
            throw new ResourceNotFoundException("Rent does not exist");
        }
        if (rent.getRentEndDate() != null) {
            throw new ResourceOccupiedException("Rent is already finished");
        }
        rentRepository.deleteRent(rent);
    }

    // TODO do zmiany
    public void updateEndRentDate(String id) throws ResourceNotFoundException, ResourceOccupiedException {
        Rent rent = rentRepository.readRentById(new MongoUUID(UUID.fromString(id)));
        if (rent == null) {
            throw new ResourceNotFoundException("Rent does not exist");
        }
        if (rent.getRentEndDate() != null) {
            throw new ResourceOccupiedException("Rent is already finished");
        }
        GregorianCalendar now = new GregorianCalendar();
        MongoUUID uuid = new MongoUUID(UUID.fromString(id));
        rentRepository.updateEndRentDate(uuid, now);
    }

    public void createRent(RentDtoPost rentDtoPost) throws ResourceNotFoundException {
        AbstractAccount account = accountRepository.readAccountById(new MongoUUID(UUID.fromString(rentDtoPost.getAccountId())));
        Room room = roomRepository.readRoomById(new MongoUUID(UUID.fromString(rentDtoPost.getRoomId())));
        if (account == null) {
            throw new ResourceNotFoundException("Account does not exist");
        }

        if (room == null) {
            throw new ResourceNotFoundException("Room does not exist");
        }

        Rent rent = new Rent(new MongoUUID(UUID.randomUUID()), rentDtoPost.getRentStartDate(), (ClientAccount) account, room);
        rentRepository.insertRent(rent);
    }

}
