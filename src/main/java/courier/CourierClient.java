package courier;
import io.qameta.allure.Step;
import models.Courier;
import models.CourierCreds;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class CourierClient {
    private static final String COURIER_ENDPOINT = "api/v1/courier";

    private static final String LOGIN_ENDPOINT = "api/v1/courier/login";

    @Step("Create courier")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT);
    }

    @Step("Sign in with courier login")
    public Response login(CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Delete courier")
    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_ENDPOINT + "/" + id);
    }
}