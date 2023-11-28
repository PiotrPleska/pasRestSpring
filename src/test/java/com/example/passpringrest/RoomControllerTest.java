package com.example.passpringrest;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.repositories.RoomRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class RoomControllerTest {

    private static Room room;
    private static RoomRepository roomRepository;

    private static final String baseUrl = "/api/rooms";

    @BeforeEach
    public void clearData() {
        roomRepository = new RoomRepository();
        roomRepository.dropRoomCollection();
        room = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 3);
        roomRepository.insertRoom(room);
    }


    @Test
    public void readRoomsTest() {
        Response response = RestAssured.get(baseUrl);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("[1]", response.jsonPath().getString("roomNumber"));
        Assertions.assertEquals("[2]", response.jsonPath().getString("roomCapacity"));
        Assertions.assertEquals("[3.0]", response.jsonPath().getString("basePrice"));
    }

    @Test
    public void readRoomsNegativeTest() {
        roomRepository.dropRoomCollection();
        Response response = RestAssured.get(baseUrl);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void readRoomByIdTest() {
        Response response = RestAssured.get(baseUrl + "/" + room.getId());
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("roomNumber"));
        Assertions.assertEquals(2, response.jsonPath().getInt("roomCapacity"));
        Assertions.assertEquals(3.0, response.jsonPath().getDouble("basePrice"));
    }

    @Test
    public void readRoomByIdNegativeTest() {
        Response response = RestAssured.get(baseUrl + "/" + UUID.randomUUID());
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void readRoomByRoomNumberNegativeTest() {
        Response response = RestAssured.get(baseUrl + "/room-number/" + 99);
        Response response2 = RestAssured.get(baseUrl + "/room-number/" + 1);
        Assertions.assertEquals(200, response2.getStatusCode());
        Assertions.assertEquals(404, response.getStatusCode());

    }

    @Test
    public void readRoomByRoomNumberTest() {
        Response response = RestAssured.get(baseUrl + "/room-number/" + room.getRoomNumber());
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("roomNumber"));
        Assertions.assertEquals(2, response.jsonPath().getInt("roomCapacity"));
        Assertions.assertEquals(3.0, response.jsonPath().getDouble("basePrice"));
    }

    @Test
    public void updateRoomPriceTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", 2);
        json.put("basePrice", 10);

        given()
                .contentType("application/json")
                .when()
                .get(baseUrl + "/room-number/" + 1)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("10.0"))
                );

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(200);

        Response response = RestAssured.get("http://localhost:8080/restapp-1.0-SNAPSHOT/api/rooms/room-number/1");
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(10.0, response.jsonPath().getDouble("basePrice"));
    }


    @Test
    public void updateRoomCapacityTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", 5);
        json.put("basePrice", 3);

        given()
                .contentType("application/json")
                .when()
                .get(baseUrl + "/room-number/" + 1)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("roomCapacity:5"))
                );

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .put(baseUrl + "/capacity/1")
                .then().statusCode(200);

        Response response = RestAssured.get(baseUrl + "/room-number/1");
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(5, response.jsonPath().getInt("roomCapacity"));
    }

    @Test
    public void deleteRoomTest() {
        Response response = RestAssured.delete(baseUrl + "/" + room.getRoomNumber());
        Assertions.assertEquals(200, response.getStatusCode());
        Response response2 = RestAssured.get(baseUrl + "/room-number/" + room.getRoomNumber());
        Assertions.assertEquals(404, response2.getStatusCode());
    }

    @Test
    public void createRoomTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 100);
        json.put("roomCapacity", 3);
        json.put("basePrice", 4);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post(baseUrl + "/add")
                .then().statusCode(200);

        Response response = RestAssured.get(baseUrl + "/room-number/100");
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(100, response.jsonPath().getInt("roomNumber"));
        Assertions.assertEquals(3, response.jsonPath().getInt("roomCapacity"));
        Assertions.assertEquals(4.0, response.jsonPath().getDouble("basePrice"));
    }
    @Test
    public void updateRoomPriceNotFoundTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 10);
        json.put("roomCapacity", 2);
        json.put("basePrice", 10);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/10")
                .then().statusCode(404);
    }

    @Test
    public void updateRoomPriceNegativeTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", 2);
        json.put("basePrice", -10);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(400);
    }

    @Test
    public void updateRoomCapacityNegativeTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", -2);
        json.put("basePrice", 3);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(400);
    }

    @Test
    public void createRoomSameRoomNumberTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", 2);
        json.put("basePrice", 3);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post(baseUrl + "/add")
                .then().statusCode(409);
    }

    @Test
    public void createRoomNegativeTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", -1);
        json.put("roomCapacity", -2);
        json.put("basePrice", -3);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post(baseUrl + "/add")
                .then().statusCode(400);
    }

    @Test
    public void deleteRoomNegativeTest() {
        Response response = RestAssured.delete(baseUrl + "/" + 99);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void deleteRentedRoomNegativeTest() {
        Room roomRented = new Room(new MongoUUID(UUID.randomUUID()), 50, 2, 3);
        roomRented.setIsRented(1);
        roomRepository.insertRoom(roomRented);
        Response response = RestAssured.delete(baseUrl + "/" + 50);
        Assertions.assertEquals(409, response.getStatusCode());
    }

}
