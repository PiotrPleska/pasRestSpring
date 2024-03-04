package integrationTests;

import com.example.passpringrest.dto.AuthenticationDto;
import com.example.passpringrest.entities.ResourceManagerAccount;
import com.example.passpringrest.entities.Room;
import com.example.passpringrest.repositories.AccountRepository;
import com.example.passpringrest.repositories.RoomRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;


@SpringBootTest(classes = com.example.passpringrest.PasSpringRestApplication.class)
public class RoomControllerTest {

    private static Room room;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final String baseUrl = "http://localhost:8080/api/rooms";

    private static String jwtToken;


    @BeforeEach
    public void clearData() {
        roomRepository.deleteAll();
        accountRepository.deleteAll();
        room = new Room(UUID.randomUUID(), 1, 2, 3);
        ResourceManagerAccount resourceManagerAccount = new ResourceManagerAccount("login", new BCryptPasswordEncoder().encode("password"),
                "12345678901", true);
        accountRepository.save(resourceManagerAccount);
        AuthenticationDto authenticationDto = new AuthenticationDto("login", "password");
        RestAssured.useRelaxedHTTPSValidation();
        ValidatableResponse response = given()
                .contentType("application/json")
                .body(authenticationDto)
                .when()
                .post("https://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200);
        jwtToken = response.extract().body().asString();
        roomRepository.save(room);
    }


    @Test
    public void readRoomsTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("[1]", response.jsonPath().getString("roomNumber"));
        Assertions.assertEquals("[2]", response.jsonPath().getString("roomCapacity"));
        Assertions.assertEquals("[3.0]", response.jsonPath().getString("basePrice"));
    }

    @Test
    public void readRoomsNegativeTest() {
        roomRepository.deleteAll();
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void readRoomByIdTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/" + room.getId());
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("roomNumber"));
        Assertions.assertEquals(2, response.jsonPath().getInt("roomCapacity"));
        Assertions.assertEquals(3.0, response.jsonPath().getDouble("basePrice"));
    }

    @Test
    public void readRoomByIdNegativeTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/" + UUID.randomUUID());
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void readRoomByRoomNumberNegativeTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/" + 99);
        Response response2 = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/" + 1);
        Assertions.assertEquals(200, response2.getStatusCode());
        Assertions.assertEquals(404, response.getStatusCode());

    }

    @Test
    public void readRoomByRoomNumberTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/" + room.getRoomNumber());
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
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/room-number/" + 1)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("10.0"))
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(200);

        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/1");
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
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/room-number/" + 1)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("roomCapacity:5"))
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .put(baseUrl + "/capacity/1")
                .then().statusCode(200);

        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/1");
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(5, response.jsonPath().getInt("roomCapacity"));
    }

    @Test
    public void deleteRoomTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).delete(baseUrl + "/" + room.getRoomNumber());
        Assertions.assertEquals(200, response.getStatusCode());
        Response response2 = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/" + room.getRoomNumber());
        Assertions.assertEquals(404, response2.getStatusCode());
    }

    @Test
    public void createRoomTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID());
        json.put("roomNumber", 100);
        json.put("roomCapacity", 3);
        json.put("basePrice", 4);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl)
                .then()
                .statusCode(201);


        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl + "/room-number/100");
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
                .header("Authorization", "Bearer " + jwtToken)
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
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(403);
    }

    @Test
    public void updateRoomCapacityNegativeTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", -2);
        json.put("basePrice", 3);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .put(baseUrl + "/price/1")
                .then().statusCode(403);
    }

    @Test
    public void createRoomSameRoomNumberTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomNumber", 1);
        json.put("roomCapacity", 2);
        json.put("basePrice", 3);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl)
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
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl)
                .then().statusCode(403);
    }

    @Test
    public void deleteRoomNegativeTest() {
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).delete(baseUrl + "/" + 99);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void deleteRentedRoomNegativeTest() {
        Room roomRented = new Room(UUID.randomUUID(), 50, 2, 3);
        roomRented.setRented(true);
        roomRepository.save(roomRented);
        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).delete(baseUrl + "/" + 50);
        Assertions.assertEquals(409, response.getStatusCode());
    }

}
