package integrationTests;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.dto.AuthenticationDto;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;
import com.example.passpringrest.repositories.AccountRepository;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AccoutControllerTest {

    private static ClientAccount clientAccount;

    private static AdminAccount adminAccount;

    private static ResourceManagerAccount resourceManagerAccount;

    private static AccountRepository accountRepository;

    private final String baseUrl = "https://localhost:8080/api/accounts";

    private static String jwtToken;

    private static String jwtTokenClient;

    private static String eTagClient;



    @BeforeEach
    public void clearData() {
        accountRepository = new AccountRepository();
        accountRepository.dropAccountCollection();
        clientAccount = new ClientAccount(new MongoUUID(UUID.randomUUID()), "client", new BCryptPasswordEncoder().encode("password"), "45032153673", true);
        adminAccount = new AdminAccount(new MongoUUID(UUID.randomUUID()), "admin", new BCryptPasswordEncoder().encode("password"), "45031103674", true);
        resourceManagerAccount = new ResourceManagerAccount(new MongoUUID(UUID.randomUUID()), "manager", new BCryptPasswordEncoder().encode("password"), "45034103675", true);
        accountRepository.insertAccount(clientAccount);
        accountRepository.insertAccount(adminAccount);
        accountRepository.insertAccount(resourceManagerAccount);
        AuthenticationDto adminDto = new AuthenticationDto("admin", "password");
        AuthenticationDto clientDto = new AuthenticationDto("client", "password");

        RestAssured.useRelaxedHTTPSValidation();
        ValidatableResponse response = given()
                .contentType("application/json")
                .body(adminDto)
                .when()
                .post("https://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200);
        jwtToken = response.extract().body().asString();

        ValidatableResponse responseClient = given()
                .contentType("application/json")
                .body(clientDto)
                .when()
                .post("https://localhost:8080/api/auth/authenticate")
                .then()
                .statusCode(200);
        jwtTokenClient = responseClient.extract().body().asString();
        eTagClient = responseClient.extract().header("ETag");

    }

    @Test
    public void readAccountsTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body(
                        containsString(adminAccount.getLogin()),
                        containsString(clientAccount.getLogin()),
                        containsString(resourceManagerAccount.getLogin())
                );
    }

    @Test
    public void readAdminAccountsTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/admins")
                .then()
                .statusCode(200)
                .body(
                        containsString(adminAccount.getLogin()),
                        not(containsString(clientAccount.getLogin())),
                        not(containsString(resourceManagerAccount.getLogin()))
                );
    }

    @Test
    public void readResourceManagerAccountsTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/resource-managers")
                .then()
                .statusCode(200)
                .body(
                        containsString(resourceManagerAccount.getLogin()),
                        not(containsString(clientAccount.getLogin())),
                        not(containsString(adminAccount.getLogin()))
                );
    }

    @Test
    public void readClientAccountsTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/clients")
                .then()
                .statusCode(200)
                .body(
                        containsString(clientAccount.getLogin()),
                        not(containsString(adminAccount.getLogin())),
                        not(containsString(resourceManagerAccount.getLogin()))
                );
    }

    @Test
    public void readAccountByIdTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/" + clientAccount.getId())
                .then()
                .statusCode(200)
                .body(
                        containsString(clientAccount.getLogin())
                );
    }

    @Test
    public void readAccountByInvalidIdTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/" + "123")
                .then()
                .statusCode(404);
    }

    @Test
    public void readAccountByPersonalIdTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/personal-id/" + clientAccount.getPersonalId())
                .then()
                .statusCode(200)
                .body(
                        containsString(clientAccount.getLogin())
                );
    }

    @Test
    public void readAccountByInvalidPersonalIdTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/personal-id/" + "1234567890")
                .then()
                .statusCode(404);
    }

    @Test
    public void readAccountByLoginTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + clientAccount.getLogin())
                .then()
                .statusCode(200)
                .body(
                        containsString(clientAccount.getLogin())
                );
    }

    @Test
    public void readAccountByInvalidLoginTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "123")
                .then()
                .statusCode(404);
    }

    @Test
    public void readAccountsByPartOfLoginTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/part-of-login/" + "adm")
                .then()
                .statusCode(200)
                .body(
                        containsString(adminAccount.getLogin())
                );
    }

    @Test
    public void readAccountsByInvalidPartOfLoginTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/part-of-login/" + "####")
                .then()
                .statusCode(403);
    }

    @Test
    public void updateAccountPasswordTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("login", clientAccount.getLogin());
        json.put("password", "noweHaslo123");
        json.put("personalId", clientAccount.getPersonalId());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtTokenClient)
                .header("If-Match", eTagClient)
                .body(json.toString())
                .when()
                .put(baseUrl + "/client/password/" + clientAccount.getLogin())
                .then().statusCode(200);

// I am not testing if password has changed because I don't have access to it
    }

    @Test
    public void updateAccountPasswordWithBadLoginTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("login", clientAccount.getLogin());
        json.put("password", "noweHaslo123");
        json.put("personalId", clientAccount.getPersonalId());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .put(baseUrl + "/password/" + "123")
                .then().statusCode(403);
    }

    @Test
    public void activateAccountTest() {

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .patch(baseUrl + "/deactivate/" + clientAccount.getLogin())
                .then().statusCode(200);

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get(baseUrl + "/" + clientAccount.getId())
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .patch(baseUrl + "/activate/" + clientAccount.getLogin())
                .then().statusCode(200);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/" + clientAccount.getId())
                .then()
                .statusCode(200)
                .body(
                        containsString("true")
                );
    }

    @Test
    public void activateAccountWithBadLoginTest() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .patch(baseUrl + "/activate/" + "123")
                .then().statusCode(404);
    }

    @Test
    public void deactivateAccountTest() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/" + clientAccount.getId())
                .then()
                .statusCode(200)
                .body(
                        not(containsString("false"))
                );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .patch(baseUrl + "/deactivate/" + clientAccount.getLogin())
                .then().statusCode(200);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/" + clientAccount.getId())
                .then()
                .statusCode(200)
                .body(
                        containsString("false")
                );
    }

    @Test
    public void deactivateAccountWithBadLoginTest() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .patch(baseUrl + "/deactivate/" + "123")
                .then().statusCode(404);
    }

    @Test
    public void createClientAccountTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowyLogin");
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103676");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin")
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/client")
                .then().statusCode(201);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin")
                .then()
                .statusCode(200)
                .body(
                        containsString("nowyLogin"),
                        containsString("45032103676")
                );
    }

    @Test
    public void createClientAccountWithAlreadyExistingLogin() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("login", clientAccount.getLogin());
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103676");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/client")
                .then().statusCode(409);
    }

    @Test
    public void createClientAccountWithAlreadyExistingPersonalId() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowyLogin");
        json.put("password", "noweHaslo123");
        json.put("personalId", clientAccount.getPersonalId());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/client")
                .then().statusCode(409);
    }

    @Test
    public void createAdminAccountTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowyLogin2");
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103677");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin2")
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/admin")
                .then().statusCode(201);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin2")
                .then()
                .statusCode(200)
                .body(
                        containsString("nowyLogin2"),
                        containsString("45032103677")
                );
    }

    @Test
    public void createAdminAccountWithAlreadyExistingLogin() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", adminAccount.getLogin());
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103677");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/admin")
                .then().statusCode(403);
    }

    @Test
    public void createAdminAccountWithAlreadyExistingPersonalId() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowyLogin2");
        json.put("password", "noweHaslo123");
        json.put("personalId", adminAccount.getPersonalId());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/admin")
                .then().statusCode(409);
    }

    @Test
    public void createResourceManagerAccountTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowyLogin3");
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103678");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin3")
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/resource-manager")
                .then().statusCode(201);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(baseUrl + "/login/" + "nowyLogin3")
                .then()
                .statusCode(200)
                .body(
                        containsString("nowyLogin3"),
                        containsString("45032103678")
                );
    }

    @Test
    public void createResourceManagerAccountWithAlreadyExistingLogin() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", resourceManagerAccount.getLogin());
        json.put("password", "noweHaslo123");
        json.put("personalId", "45032103678");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/resource-manager")
                .then().statusCode(409);
    }




    @Test
    public void createAccountWithInvalidDataTest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", new MongoUUID(UUID.randomUUID()));
        json.put("login", "nowy");
        json.put("password", "nowe");
        json.put("personalId", "1234567890");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .body(json.toString())
                .when()
                .post(baseUrl + "/client")
                .then().statusCode(403);
    }

}
