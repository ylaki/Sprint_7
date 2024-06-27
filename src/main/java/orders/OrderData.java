package orders;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderData {
    private static final String ORDER_ENDPOINT = "/api/v1/orders";

    @Step("Create order")
    public Response createOrder(String payload) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(payload)
                .when()
                .post(ORDER_ENDPOINT);
    }

    public Response getOrdersList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_ENDPOINT);
    }
}