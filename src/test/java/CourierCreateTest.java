import courier.*;
import io.qameta.allure.Step;
import models.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.ResponseBody;

import static courier.CourierGenerator.*;
import static courier.CourierGenerator.randomCourier;
import static models.CourierCreds.credsFromCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierCreateTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    private CourierClient courierClient;
    private int id;
    private int CurrentStatusCode;

    @Step ("Set BaseURI")
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Create courier")
    @Description("Create courier with random data")
    public void createCourier() {
        Courier courier = randomCourier();

        Response response = courierClient.create(courier);
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        CurrentStatusCode = response.statusCode();

        compareCourierCreationStatusCode201(CurrentStatusCode);
        comparePayload(bodyAsString);

        id = obtainID(courier);
    }

    @Step("Compare status code 201 with body")
    public void compareCourierCreationStatusCode201(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_CREATED, CurrentStatusCode);
    }

    @Step("Compare expected body with response")
    public void comparePayload(String bodyAsString) {
        assertTrue(bodyAsString.contains("\"ok\":true"));
    }

    @Test
    @DisplayName("Create courier")
    @Description("Create courier with duplicated login")
    public void createDuplicatedCourier() {

        Courier courier = nonRandomCourier();
        courierClient.create(courier);
        courierClient.create(courier);

        Response response = courierClient.create(courier);
        CurrentStatusCode = response.statusCode();
        compareCourierCreationStatusCode409(CurrentStatusCode);
        id = obtainID(courier);
    }

    @Test
    @DisplayName("Create courier")
    @Description("Create courier without login")
    public void createCourierWithoutLogin() {
        Courier courier = noLoginCourier();

        Response response = courierClient.create(courier);
        CurrentStatusCode = response.statusCode();

        compareCourierCreationStatusCode400(CurrentStatusCode);
    }

    @Test
    @DisplayName("Create courier")
    @Description("Create courier without password")
    public void createCourierWithoutPassword() {
        Courier courier = noPasswordCourier();

        Response response = courierClient.create(courier);
        CurrentStatusCode = response.statusCode();

        compareCourierCreationStatusCode400(CurrentStatusCode);
    }

    @Step("Compare status code 409 with response")
    public void compareCourierCreationStatusCode409(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_CONFLICT, CurrentStatusCode);
    }

    @Step("Compare status code 400 with response")
    public void compareCourierCreationStatusCode400(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_BAD_REQUEST, CurrentStatusCode);
    }

    @Step("Enter and get courier ID")
    public int obtainID(Courier courier){
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        return loginResponse.path("id");
    }

    @Step("Delete courier")
    @After
    public void tearDown() {
        courierClient.delete(id);
    }

}