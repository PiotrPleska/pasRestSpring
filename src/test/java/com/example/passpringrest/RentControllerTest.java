package com.example.passpringrest;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.Rent;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.repositories.AccountRepository;
import com.example.passpringrest.repositories.RentRepository;
import com.example.passpringrest.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.GregorianCalendar;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class RentControllerTest {

    private static Rent rent;

    private static Rent rentFinished;
    private static RentRepository rentRepository;

    private static RoomRepository roomRepository;

    private static AccountRepository accountRepository;

    private final String baseUrl = "/api/rents";

    @BeforeEach
    public void clearData() {
        rentRepository = new RentRepository();
        accountRepository = new AccountRepository();
        roomRepository = new RoomRepository();

        rentRepository.dropRentCollection();
        accountRepository.dropAccountCollection();
        roomRepository.dropRoomCollection();

        //Set activeRents to empty array

        ClientAccount clientAccount = new ClientAccount(new MongoUUID(UUID.randomUUID()), "korwinkrul123", "haslo123", "45032103673", true);
        Room room = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 3);
        Room room2 = new Room(new MongoUUID(UUID.randomUUID()), 2, 2, 3);

        accountRepository.insertAccount(clientAccount);
        roomRepository.insertRoom(room);
        roomRepository.insertRoom(room2);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.YEAR, 2000);
        rentFinished = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), clientAccount, room);
        rent = new Rent(new MongoUUID(UUID.randomUUID()),calendar , clientAccount, room2);

        rentRepository.insertRent(rentFinished);
        rentRepository.deleteRent(rentFinished);
        rentRepository.insertRent(rent);
    }

    @Test
    public void readRentsTest() {
        given()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString()),
                        containsString(rentFinished.getRentId().getUuid().toString())
                );
    }

    @Test
    public void readAllRentsWhenEmptyTest() {
        rentRepository.dropRentCollection();
        given()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(404);
    }

    @Test
    public void readRentByIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString())
                );
    }

    @Test
    public void readRentByIdWhenNotFoundTest() {
        given()
                .when()
                .get(baseUrl + "/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }


    @Test
    public void readCurrentRentsTest() {
        given()
                .when()
                .get(baseUrl + "/" + "current")
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString()),
                        not(containsString(rentFinished.getRentId().getUuid().toString()))
                );
    }

    @Test
    public void readCurrentRentsWhenEmptyTest() {
        rentRepository.dropRentCollection();
        given()
                .when()
                .get(baseUrl + "/" + "current")
                .then()
                .statusCode(404);
    }

    @Test
    public void readPastRentsTest() {
        given()
                .when()
                .get(baseUrl + "/" + "past")
                .then()
                .statusCode(200)
                .body(
                        not(containsString(rent.getRentId().getUuid().toString())),
                        containsString(rentFinished.getRentId().getUuid().toString())
                );
    }

    @Test
    public void readPastRentsWhenEmptyTest() {
        rentRepository.dropRentCollection();
        given()
                .when()
                .get(baseUrl + "/" + "past")
                .then()
                .statusCode(404);
    }

    @Test
    public void readCurrentRentsByRoomIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "current/room-id/" + rent.getRoom().getId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString()),
                        not(containsString(rentFinished.getRentId().getUuid().toString()))
                );
    }

    @Test
    public void readCurrentRentsByRoomIdWithWrongIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "current/room-id/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void readPastRentsByRoomIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "past/room-id/" + rentFinished.getRoom().getId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        not(containsString(rent.getRentId().getUuid().toString())),
                        containsString(rentFinished.getRentId().getUuid().toString())
                );
    }

    @Test
    public void readPastRentsByRoomIdWithWrongIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "past/room-id/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void readCurrentRentsByClientIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "current/account-id/" + rent.getClientAccount().getId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString()),
                        not(containsString(rentFinished.getRentId().getUuid().toString()))
                );
    }

    @Test
    public void readCurrentRentsByClientIdWithWrongIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "current/account-id/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void readPastRentsByClientIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "past/account-id/" + rentFinished.getClientAccount().getId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        not(containsString(rent.getRentId().getUuid().toString())),
                        containsString(rentFinished.getRentId().getUuid().toString())
                );
    }

    @Test
    public void readPastRentsByClientIdWithWrongIdTest() {
        given()
                .when()
                .get(baseUrl + "/" + "past/account-id/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteRentTest() {

        given()
                .when()
                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        not(containsString("2023"))
                );

        given()
                .when()
                .delete(baseUrl + "/" + rent.getRentId().getUuid().toString())
                .then()
                .statusCode(200);

        given()
                .when()
                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
                .then()
                .statusCode(200)
                .body(
                        containsString("2023")
                );
    }

    @Test
    public void deleteFinishedRentTest() {
        given()
                .when()
                .delete(baseUrl + "/" + rentFinished.getRentId().getUuid().toString())
                .then()
                .statusCode(409);

        given()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body(
                        containsString(rent.getRentId().getUuid().toString()),
                        containsString(rentFinished.getRentId().getUuid().toString())
                );
    }

    @Test
    public void deleteRentWhenNotFoundTest() {
        given()
                .when()
                .delete(baseUrl + "/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }
}
