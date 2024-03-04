//package integrationTests;
//
//import com.example.passpringrest.codecs.MongoUUID;
//import com.example.passpringrest.dto.AuthenticationDto;
//import com.example.passpringrest.entities.ClientAccount;
//import com.example.passpringrest.entities.Rent;
//import com.example.passpringrest.entities.ResourceManagerAccount;
//import com.example.passpringrest.entities.Room;
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import io.restassured.response.ValidatableResponse;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//
//import java.time.Year;
//import java.util.GregorianCalendar;
//import java.util.UUID;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.not;
//
//public class RentControllerTest {
//    private static Rent rent;
//
//    private static Rent rentFinished;
//    private static RentRepository rentRepository;
//
//    private static RoomRepository roomRepository;
//
//    private final String baseUrl = "https://localhost:8080/api/rents";
//
//    private static String jwtToken;
//
//    private static ClientAccount clientAccount;
//
//    private static Room room;
//
//    private static Room room2;
//
//
//    @BeforeEach
//    public void clearData() {
//        rentRepository = new RentRepository();
//        AccountRepository accountRepository = new AccountRepository();
//        roomRepository = new RoomRepository();
//
//        rentRepository.dropRentCollection();
//        accountRepository.dropAccountCollection();
//        roomRepository.dropRoomCollection();
//
//        //Set activeRents to empty array
//
//        clientAccount = new ClientAccount(new MongoUUID(UUID.randomUUID()), "client", new BCryptPasswordEncoder().encode("password"), "45032103673", true);
//        ResourceManagerAccount resourceManagerAccount = new ResourceManagerAccount(new MongoUUID(UUID.randomUUID()), "manager", new BCryptPasswordEncoder().encode("password"), "45032103674", true);
//        room = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 3);
//        room2 = new Room(new MongoUUID(UUID.randomUUID()), 2, 2, 3);
//
//        accountRepository.insertAccount(clientAccount);
//        accountRepository.insertAccount(resourceManagerAccount);
//        roomRepository.insertRoom(room);
//        roomRepository.insertRoom(room2);
//
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.set(GregorianCalendar.YEAR, 2000);
//        rentFinished = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), clientAccount, room);
//        rent = new Rent(new MongoUUID(UUID.randomUUID()),calendar , clientAccount, room2);
//
//        rentRepository.insertRent(rentFinished);
//        rentRepository.deleteRent(rentFinished);
//        rentRepository.insertRent(rent);
//
//        AuthenticationDto managerDto = new AuthenticationDto("manager", "password");
//        AuthenticationDto clientDto = new AuthenticationDto("client", "password");
//
//
//        RestAssured.useRelaxedHTTPSValidation();
//        ValidatableResponse response = given()
//                .contentType("application/json")
//                .body(managerDto)
//                .when()
//                .post("https://localhost:8080/api/auth/authenticate")
//                .then()
//                .statusCode(200);
//        jwtToken = response.extract().body().asString();
//    }
//
//    @Test
//    public void readRentsTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl)
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString()),
//                        containsString(rentFinished.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void readAllRentsWhenEmptyTest() {
//        rentRepository.dropRentCollection();
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl)
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readRentByIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void readRentByIdWhenNotFoundTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + UUID.randomUUID())
//                .then()
//                .statusCode(403);
//    }
//
//
//    @Test
//    public void readCurrentRentsTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current")
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString()),
//                        not(containsString(rentFinished.getRentId().getUuid().toString()))
//                );
//    }
//
//    @Test
//    public void readCurrentRentsWhenEmptyTest() {
//        rentRepository.dropRentCollection();
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readPastRentsTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past")
//                .then()
//                .statusCode(200)
//                .body(
//                        not(containsString(rent.getRentId().getUuid().toString())),
//                        containsString(rentFinished.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void readPastRentsWhenEmptyTest() {
//        rentRepository.dropRentCollection();
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readCurrentRentsByRoomIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current/room-id/" + rent.getRoom().getId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString()),
//                        not(containsString(rentFinished.getRentId().getUuid().toString()))
//                );
//    }
//
//    @Test
//    public void readCurrentRentsByRoomIdWithWrongIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current/room-id/" + UUID.randomUUID())
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readPastRentsByRoomIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past/room-id/" + rentFinished.getRoom().getId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        not(containsString(rent.getRentId().getUuid().toString())),
//                        containsString(rentFinished.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void readPastRentsByRoomIdWithWrongIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past/room-id/" + UUID.randomUUID())
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readCurrentRentsByClientIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current/account-id/" + rent.getClientAccount().getId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString()),
//                        not(containsString(rentFinished.getRentId().getUuid().toString()))
//                );
//    }
//
//    @Test
//    public void readCurrentRentsByClientIdWithWrongIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/current/account-id/" + UUID.randomUUID())
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void readPastRentsByClientIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past/account-id/" + rentFinished.getClientAccount().getId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        not(containsString(rent.getRentId().getUuid().toString())),
//                        containsString(rentFinished.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void readPastRentsByClientIdWithWrongIdTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/past/account-id/" + UUID.randomUUID())
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void deleteRentTest() {
//
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        not(containsString(String.valueOf(Year.now().getValue())))
//                );
//
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .delete(baseUrl + "/" + rent.getRentId().getUuid().toString())
//                .then()
//                .statusCode(200);
//
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl + "/" + rent.getRentId().getUuid().toString())
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(String.valueOf(Year.now().getValue()))
//                );
//    }
//
//    @Test
//    public void deleteFinishedRentTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .delete(baseUrl + "/" + rentFinished.getRentId().getUuid().toString())
//                .then()
//                .statusCode(409);
//
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .get(baseUrl)
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString(rent.getRentId().getUuid().toString()),
//                        containsString(rentFinished.getRentId().getUuid().toString())
//                );
//    }
//
//    @Test
//    public void deleteRentWhenNotFoundTest() {
//        given()
//                .header("Authorization", "Bearer " + jwtToken)
//                .when()
//                .delete(baseUrl + UUID.randomUUID())
//                .then()
//                .statusCode(403);
//    }
//
//    @Test
//    public void createRentTest() throws JSONException {
//        Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl);
//        int size = response.jsonPath().getList("$").size();
//        Room newRoom = new Room(new MongoUUID(UUID.randomUUID()), 3, 2, 3);
//        roomRepository.insertRoom(newRoom);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("accountId", clientAccount.getId().getUuid().toString());
//        jsonObject.put("roomId", newRoom.getId().getUuid().toString());
//        jsonObject.put("rentStartDate", "2024-02-18T20:52:00+01");
//        given()
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + jwtToken)
//                .body(jsonObject.toString())
//                .when()
//                .post(baseUrl)
//                .then()
//                .statusCode(201);
//
//        response = RestAssured.given().header("Authorization", "Bearer " + jwtToken).get(baseUrl);
//        Assertions.assertEquals(size + 1, response.jsonPath().getList("$").size());
//    }
//
//    @Test
//    public void createRentWhenAccountNotFoundTest() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("accountId", UUID.randomUUID().toString());
//        jsonObject.put("roomId", room.getId().getUuid().toString());
//        jsonObject.put("rentStartDate", "30-11-2023 14:30:00");
//
//        given()
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + jwtToken)
//                .body(jsonObject.toString())
//                .when()
//                .post(baseUrl)
//                .then()
//                .statusCode(403);
//    }
//
//    @Test
//    public void createRentWhenRoomNotFoundTest() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("accountId", clientAccount.getId().getUuid().toString());
//        jsonObject.put("roomId", UUID.randomUUID().toString());
//        jsonObject.put("rentStartDate", "30-11-2023 14:30:00");
//
//        given()
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + jwtToken)
//                .body(jsonObject.toString())
//                .when()
//                .post(baseUrl)
//                .then()
//                .statusCode(403);
//    }
//
//    @Test
//    public void createRentWhenRoomIsRentedTest() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("accountId", clientAccount.getId().getUuid().toString());
//        jsonObject.put("roomId", room2.getId().getUuid().toString());
//        jsonObject.put("rentStartDate", "30-11-2023 14:30:00");
//
//        given()
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + jwtToken)
//                .body(jsonObject.toString())
//                .when()
//                .post(baseUrl)
//                .then()
//                .statusCode(403);
//    }
//}
