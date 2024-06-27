import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)

public class OrderCreateTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private OrderData orderData;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public OrderCreateTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                new Object[] {
                        "Angela", "Adminson", "Osborne park, 55", 4, "+7 800 123 12 12", 5, "2024-07-10",
                        "Please arrive in the morning", new String[] {"BLACK"}
                },
                new Object[] {
                        "Adam", "Adams", "Kings street, 55", 26, "+7 999 555 66 77", 5, "2024-07-10",
                        "Please arrive in the morning", new String[] {"GREY"}
                },
                new Object[] {
                        "Adam", "Adams", "Kings street, 55", 26, "+7 999 555 66 77", 5, "2024-07-10",
                        "Please arrive in the morning", new String[] {"GREY", "BLACK"}
                },
                new Object[] {
                        "Adam", "Adams", "Kings street, 55", 26, "+7 999 555 66 77", 5, "2024-07-10",
                        "Please arrive in the morning",new String[] {}
                }
        };
    }

    @Test
    @DisplayName("Create order")
    @Description("Create order with parametrization")
    public void createOrderCheck() {
        String payload = "{"
                + "\"firstName\": \"" + firstName + "\","
                + "\"lastName\": \"" + lastName + "\","
                + "\"address\": \"" + address + "\","
                + "\"metroStation\": " + metroStation + ","
                + "\"phone\": \"" + phone + "\","
                + "\"rentTime\": " + rentTime + ","
                + "\"deliveryDate\": \"" + deliveryDate + "\","
                + "\"comment\": \"" + comment + "\","
                + "\"color\": [\"" + String.join("\", \"", color) + "\"]"
                + "}";

        orderData = new OrderData();
        Response response = orderData.createOrder(payload);

        String responseBody = response.getBody().asString();
        boolean isTrack = responseBody.contains("\"track\"");

        verifyTrackInResponse(isTrack);
    }
    @Step("Verify track in the response")
    public void verifyTrackInResponse(boolean isTrack) {
        assertTrue("Response body does not contain 'track'", isTrack);
    }
}