import courier.*;
import io.qameta.allure.Step;
import io.restassured.response.ResponseBody;
import models.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static courier.CourierGenerator.randomCourier;
import static models.CourierCreds.credsFromCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    private CourierClient courierClient;
    private int id;
    private int CurrentStatusCode;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Courier login")
    @Description("Courier login in the system")
    public void loginCourierCheck() {

        Courier courier = randomCourier();
        courierClient.create(courier);

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        CurrentStatusCode = loginResponse.statusCode();
        ResponseBody body = loginResponse.getBody();
        String bodyAsString = body.asString();

        compareCourierCreationStatusCode200(CurrentStatusCode);
        verifyIdReturn(bodyAsString);

        id = obtainID(courier);
    }

    @Step("Compare status code 201 with response")
    public void compareCourierCreationStatusCode200(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_OK, CurrentStatusCode);
    }

    @Step("Verify returned ID")
    public void verifyIdReturn(String bodyAsString) {
        assertTrue(bodyAsString.contains("id"));
    }

    @Test
    @DisplayName("Login fields")
    @Description("Verification of mandatory fields")
    public void mandatoryLoginFieldsCheck(){

        Courier courier = randomCourier();
        courierClient.create(courier);

        emptyLoginCheck(courier);
        emptyPasswordCheck(courier);

        id = obtainID(courier);
    }

    @Step("Sign in verification with empty login")
    public void emptyLoginCheck(Courier courier){
        CourierCreds creds = new CourierCreds(courier.getLogin(), "");
        Response loginResponse = courierClient.login(creds);
        CurrentStatusCode = loginResponse.statusCode();
        assertEquals("Incorrect status code", SC_BAD_REQUEST, CurrentStatusCode);
    }

    @Step("Sign in verification with empty password")
    public void emptyPasswordCheck(Courier courier){
        CourierCreds creds = new CourierCreds("", courier.getPassword());
        Response loginResponse = courierClient.login(creds);
        CurrentStatusCode = loginResponse.statusCode();
        assertEquals("Incorrect status code", SC_BAD_REQUEST, CurrentStatusCode);
    }

    @Step("Verify returned ID")
    public int obtainID(Courier courier){
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        return loginResponse.path("id");
    }

    @Test
    @DisplayName("Invalid login or password")
    @Description("Verify the error response with invalid login or password")
    public void incorrectCredsCheck() {

        Courier courier = randomCourier();
        courierClient.create(courier);

        incorrectLoginCheck(courier);
        incorrectPasswordCheck(courier);

        id = obtainID(courier);
    }

    @Step("Sign in with invalid login")
    public void incorrectLoginCheck(Courier courier){
        CourierCreds creds = new CourierCreds("kjfnskjfnwje7yh3t65tg3bhdc6tgjnc",courier.getLogin());
        Response loginResponse = courierClient.login(creds);
        CurrentStatusCode = loginResponse.statusCode();
        assertEquals("Incorrect status code", SC_NOT_FOUND, CurrentStatusCode);
    }

    @Step("Sign in with invalid password")
    public void incorrectPasswordCheck(Courier courier){
        CourierCreds creds = new CourierCreds(courier.getLogin(),"kjfnskjfnwje7yh3t65tg3bhdc6tgjnc");
        Response loginResponse = courierClient.login(creds);
        CurrentStatusCode = loginResponse.statusCode();
        assertEquals("Incorrect status code", SC_NOT_FOUND, CurrentStatusCode);
    }

    @Step("Delete courier by ID")
    @After
    public void tearDown() {
        courierClient.delete(id);
    }
}