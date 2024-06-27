import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderData;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrdersListTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private OrderData orderData;
    private int CurrentStatusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Get orders list")
    @Description("Get orders list by parameter")
    public void testOrderListEndpointWithNearestStationFilter() {
        orderData = new OrderData();
        Response response = orderData.getOrdersList();

        String responseBody = response.getBody().asString();
        boolean isOrders = responseBody.contains("orders");
        CurrentStatusCode = response.statusCode();

        verifyOrdersStatusCode(CurrentStatusCode);
        verifyOrdersInResponse(isOrders);
    }

    @Step("Verify response 200")
    public void verifyOrdersStatusCode(int CurrentStatusCode) {
        assertEquals("Incorrect status code", SC_OK, CurrentStatusCode);
    }

    @Step("Verify orders in the response")
    public void verifyOrdersInResponse(boolean isOrders) {
        assertTrue("Response body does not contain 'orders'", isOrders);
    }
}
