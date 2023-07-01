import data.Courier;
import data.Orders;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Client;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static courier.Generator.randomCourier;
import static data.CourierCert.certFrom;
import static order.Generator.randomOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Utils.randomInt;

public class TestAcceptTheOrder {
    private static courier.Client courierClient;
    private static Client client;

    private Courier courier;
    private int courierId;
    private Orders orders;
    private int trackId;
    private int orderId;

    @BeforeClass
    public static void setupClass() {
        courierClient = new courier.Client();
        client = new Client();
    }

    @Before
    public void setup() {
        courier = randomCourier();
        courierClient.create(courier);

        ValidatableResponse loginResponse = courierClient.login(certFrom(courier));
        courierId = loginResponse.extract().path("id");

        orders = randomOrder();
        trackId = client.create(orders).extract().path("track");

        ValidatableResponse getOrderResponse = client.getOrdersByCourier(String.valueOf(trackId));
        orderId = getOrderResponse.extract().path("order.id");
    }

    @Test
    @DisplayName("Accept order")
    @Description("Проверка принятия заказа")
    public void testAcceptOrder() {
        ValidatableResponse response = client.acceptOrder(String.valueOf(orderId), String.valueOf(courierId));
        assertThat("Статус код неверный при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("ok"), equalTo(true));
    }

    @Test
    @DisplayName("Accept order without courierId")
    @Description("Проверка принятия заказа без id курьера")
    public void testAcceptOrderWithoutCourierId() {
        ValidatableResponse response = client.acceptOrder(String.valueOf(orderId), "");
        assertThat("Статус код неверный при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order with invalid courierId")
    @Description("Проверка принятия заказа c несуществующим id курьера")
    public void testAcceptOrderWithInvalidCourierId() {
        ValidatableResponse response = client.acceptOrder(String.valueOf(orderId), String.valueOf(randomInt(1000, 2000)));
        assertThat("Статус код неверный при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order with invalid orderId")
    @Description("Проверка принятия заказа c несуществующим id заказа")
    public void testAcceptOrderWithInvalidOrderId() {
        ValidatableResponse response = client.acceptOrder(String.valueOf(-1), String.valueOf(courierId));
        assertThat("Статус код неверный при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Заказа с таким id не существует"));
    }

    @After
    public void tearDown() {
        client.cancelOrder(trackId);
        courierClient.delete(courierId);
    }
}
