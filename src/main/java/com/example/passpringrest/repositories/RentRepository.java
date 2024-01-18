package com.example.passpringrest.repositories;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.Rent;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.exceptions.RentTransactionException;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.example.passpringrest.exceptions.ResourceOccupiedException;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

@Repository
public class RentRepository extends AbstractMongoRepository {

    private final MongoCollection<Rent> rentCollection;

    private final AccountRepository accountRepository;

    private final RoomRepository roomRepository;

    public RentRepository() {
        this.rentCollection = initDbConnection().getCollection("rents", Rent.class);
        this.accountRepository = new AccountRepository();
        this.roomRepository = new RoomRepository();
    }

    public void insertRent(Rent rent) throws RentTransactionException, ResourceOccupiedException, ResourceNotFoundException {
        ClientSession clientSession = mongoClient.startSession();

        addRentToDocument(rent, clientSession); // if this does not error out, we can add rent to db

        MongoCollection<Rent> rentCollection = db.getCollection("rents", Rent.class).withWriteConcern(WriteConcern.MAJORITY);

        rentCollection.insertOne(rent);

        clientSession.close();
    }

    private Document rentToDocument(Rent rent) {
        return new Document("roomNumber", rent.getRoom().getRoomNumber());
    }

    // This method in created to have sure that we can add rent only if room is not rented
    private void addRentToDocument(Rent rent, ClientSession clientSession) throws RentTransactionException, ResourceOccupiedException,
            ResourceNotFoundException {
        try {
            MongoCollection<AbstractAccount> clientCollection =
                    db.getCollection("accounts", AbstractAccount.class).withWriteConcern(WriteConcern.MAJORITY);

            if (clientCollection.find(Filters.eq("personalId", rent.getClientAccount().getPersonalId())).first() == null) {
                System.err.println("Could not find client account with personalId: " + rent.getClientAccount().getPersonalId());
                throw new ResourceNotFoundException("Could not find client account with personalId: " + rent.getClientAccount().getPersonalId());
            }

            MongoCollection<Room> roomCollection = db.getCollection("rooms", Room.class).withWriteConcern(WriteConcern.MAJORITY);
            if (roomCollection.find(Filters.eq("roomNumber", rent.getRoom().getRoomNumber())).first() == null) {
                System.err.println("Could not find room with roomNumber: " + rent.getRoom().getRoomNumber());
                throw new ResourceNotFoundException("Could not find room with roomNumber: " + rent.getRoom().getRoomNumber());
            }
            MongoCollection<Document> activeRentsCollection =
                    db.getCollection("activeRents").withWriteConcern(WriteConcern.MAJORITY);

            clientSession.startTransaction(); // We want to have transaction here, we don't need to check for
            // user and for room inside transaction, because we are not changing anything in db, we are just reading

            List<Document> activeRents = activeRentsCollection.find().first().getList("activeRents", Document.class);
            boolean isRented = activeRents.stream().anyMatch(activeRent -> activeRent.getInteger("roomNumber") == rent.getRoom().getRoomNumber());

            if (isRented) {
                System.err.println("Room is already rented");
                throw new ResourceOccupiedException("Room is already rented");
            }

            Document rentDocument = rentToDocument(rent);

            Bson filter = Filters.eq("_id", new ObjectId("6568aeca3aeb7ad9264d8f33"));
            Bson update = Updates.push("activeRents", rentDocument);

            activeRentsCollection.updateOne(filter, update);

            clientSession.commitTransaction();

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (ResourceOccupiedException e) {
            clientSession.abortTransaction();
            throw e;
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw new RentTransactionException(e.getMessage());
        }
    }

    // We assume that we can end rent only by room number, you can Rent only from "now"
    public void deleteRent(Rent rent) throws RentTransactionException, ResourceNotFoundException {
        try (ClientSession clientSession = mongoClient.startSession()) {
            try {
                int roomNumber = rent.getRoom().getRoomNumber();
                MongoCollection<Document> activeRentsCollection =
                        db.getCollection("activeRents").withWriteConcern(WriteConcern.MAJORITY);

                clientSession.startTransaction();

                List<Document> activeRents = activeRentsCollection.find().first().getList("activeRents", Document.class);
                Document rentDocument =
                        activeRents.stream().filter(activeRent -> activeRent.getInteger("roomNumber") == roomNumber).findFirst().orElse(null);

                Bson filter = Filters.eq("_id", new ObjectId("6568aeca3aeb7ad9264d8f33"));
                Bson update = Updates.pull("activeRents", rentDocument);

                activeRentsCollection.updateOne(filter, update);

                clientSession.commitTransaction();

                // Update the main collection, not just the activeRents Mainly set rentEndDate to now
                updateEndRentDate(rent.getRentId(), new GregorianCalendar());
            } catch (Exception e) {
                clientSession.abortTransaction();
                throw new RentTransactionException(e.getMessage());
            }
        }
    }

    public void updateEndRentDateByRent(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getRentId());
        Bson setUpdate = Updates.set("rentEndDate", rent.getRentEndDate());
        List<Bson> updates = new ArrayList<>();
        updates.add(setUpdate);
        rentCollection.updateOne(filter, updates);
    }

    public void updateEndRentDate(MongoUUID id, GregorianCalendar rentEndDate) throws ResourceNotFoundException {
        try {
            readRentById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find rent with id: " + id);
        }
        Bson filter = Filters.eq("_id", id);
        Bson setUpdate = Updates.set("rentEndDate", rentEndDate);
        List<Bson> updates = new ArrayList<>();
        updates.add(setUpdate);
        rentCollection.updateOne(filter, updates);
    }

    public Rent readRentById(MongoUUID id) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.eq("_id", id);
            Rent rent = rentCollection.find(filter).first();
            if (rent == null) {
                throw new ResourceNotFoundException("Could not find rent with id: " + id);
            }
            return rent;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find rent with id: " + id);
        }
    }

    public List<Rent> readRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accountRepository.readAccountById(new MongoUUID(UUID.fromString(id)));
            if (account == null) {
                throw new ResourceNotFoundException("Could not find account with id: " + id);
            }
            Bson filter = Filters.eq("account", account);
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find rent with account id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find account with id: " + id + "or there is no rents for this account");
        }
    }

    public List<Rent> readAllCurrentRents() throws ResourceNotFoundException {
        try {
            Bson filter = Filters.exists("rentEndDate", false);
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find any current rents");
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find any current rents");
        }
    }

    public List<Rent> readAllPastRents() throws ResourceNotFoundException {
        try {
            Bson filter = Filters.exists("rentEndDate", true);
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find any past rents");
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find any past rents");
        }
    }

    public List<Rent> readAllCurrentRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accountRepository.readAccountById(new MongoUUID(UUID.fromString(id)));
            if (account == null) {
                throw new ResourceNotFoundException("Could not find account with id: " + id);
            }
            Bson filter = Filters.and(Filters.eq("account", account), Filters.exists("rentEndDate", false));
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find current rents for account with id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find account with id: " + id + "or there is no current rents for this account");
        }
    }

    public List<Rent> readAllPastRentsByAccountId(String id) throws ResourceNotFoundException {
        try {
            AbstractAccount account = accountRepository.readAccountById(new MongoUUID(UUID.fromString(id)));
            if (account == null) {
                throw new ResourceNotFoundException("Could not find account with id: " + id);
            }
            Bson filter = Filters.and(Filters.eq("account", account), Filters.exists("rentEndDate", true));
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find past rents for account with id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find account with id: " + id + "or there is no past rents for this account");
        }
    }

    public List<Rent> readRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            Room room = roomRepository.readRoomById(new MongoUUID(UUID.fromString(id)));
            if (room == null) {
                throw new ResourceNotFoundException("Could not find room with id: " + id);
            }
            Bson filter = Filters.eq("room", room);
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find rent with room id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find room with id: " + id + "or there is no rents for this room");
        }
    }

    public List<Rent> readAllCurrentRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            Room room = roomRepository.readRoomById(new MongoUUID(UUID.fromString(id)));
            if (room == null) {
                throw new ResourceNotFoundException("Could not find room with id: " + id);
            }
            Bson filter = Filters.and(Filters.eq("room", room), Filters.exists("rentEndDate", false));
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find current rents for room with id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find room with id: " + id + "or there is no current rents for this room");
        }
    }

    public List<Rent> readAllPastRentsByRoomId(String id) throws ResourceNotFoundException {
        try {
            Room room = roomRepository.readRoomById(new MongoUUID(UUID.fromString(id)));
            if (room == null) {
                throw new ResourceNotFoundException("Could not find room with id: " + id);
            }
            Bson filter = Filters.and(Filters.eq("room", room), Filters.exists("rentEndDate", true));
            List<Rent> rents = rentCollection.find(filter).into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find past rents for room with id: " + id);
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find room with id: " + id + "or there is no past rents for this room");
        }
    }


    public List<Rent> readAllRents() throws ResourceNotFoundException {
        try {
            List<Rent> rents = rentCollection.find().into(new ArrayList<>());
            if (rents.isEmpty()) {
                throw new ResourceNotFoundException("Could not find any rents");
            }
            return rents;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not find any rents");
        }
    }

    public void dropRentCollection() {
        rentCollection.drop();
        // Set active rents to empty array
        MongoCollection<Document> activeRentsCollection = db.getCollection("activeRents");
        Document updateDocument = new Document("$set", new Document("activeRents", new ArrayList<>()));
        activeRentsCollection.updateMany(new Document(), updateDocument);
    }

}
