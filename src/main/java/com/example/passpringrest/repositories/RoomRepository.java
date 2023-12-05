package com.example.passpringrest.repositories;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomRepository extends AbstractMongoRepository {

    private MongoCollection<Room> roomCollection;

    public RoomRepository() {
        this.roomCollection = initDbConnection().getCollection("rooms", Room.class);
        Document index = new Document("roomNumber", 1);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        roomCollection.createIndex(index, indexOptions);
    }

    public void insertRoom(Room room) {
        roomCollection.insertOne(room);
    }

    public List<Room> readAllRooms() {
        try {
            return roomCollection.find().into(new ArrayList<>());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No rooms found");
        }
    }

    public Room readRoomById(MongoUUID id) {
        try {
            Bson filter = Filters.eq("_id", id);
            return roomCollection.find(filter).first();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given id");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Something went wrong");
        }
    }

    public Room readRoomByRoomNumber(int roomNumber) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.eq("roomNumber", roomNumber);
            return roomCollection.find(filter).first();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
    }

    public Room readRoomByRoomNumberWithoutException(int roomNumber) {
        Bson filter = Filters.eq("roomNumber", roomNumber);
        return roomCollection.find(filter).first();
    }

    public void updateRoomPrice(int roomNumber, double newPrice) throws ResourceNotFoundException {
        try {
            readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        Bson filter = Filters.eq("roomNumber", roomNumber);
        Bson setUpdate = Updates.set("basePrice", newPrice);
        roomCollection.updateOne(filter, setUpdate);
    }

    public void updateRoomCapacity(int roomNumber, int newCapacity) throws ResourceNotFoundException {
        try {
            readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        Bson filter = Filters.eq("roomNumber", roomNumber);
        Bson setUpdate = Updates.set("roomCapacity", newCapacity);
        roomCollection.updateOne(filter, setUpdate);
    }

    public void deleteRoom(int roomNumber) throws ResourceNotFoundException {
        try {
            readRoomByRoomNumber(roomNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No room with given room number");
        }
        Bson filter = Filters.eq("roomNumber", roomNumber);
        roomCollection.deleteOne(filter);
    }

    public void dropRoomCollection() {
        roomCollection.drop();
        roomCollection = initDbConnection().getCollection("rooms", Room.class);
        Document index = new Document("roomNumber", 1);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        roomCollection.createIndex(index, indexOptions);
    }
}
