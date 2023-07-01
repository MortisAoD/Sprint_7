import data.Courier;
import data.Orders;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Client;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static courier.Generator.randomCourier;
import static data.CourierCert.certFrom;
import static order.Generator.randomOrder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.Utils.randomInt;

public class TestGetOrderByNum {

    private courier.Client courierClient;
    private Client client;
    private int courierId;
    private int trackId;

    @Before
    public void setUp() {
        Courier courier = randomCourier();
        courierClient = new courier.Client();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(certFrom(courier));
        courierId = loginResponse.extract().path("id");

        client = new Client();
        Orders order = randomOrder();
        ValidatableResponse createOrderResponse = client.create(order);
        trackId = createOrderResponse.extract().path("track");
    }

    @Test
    @DisplayName("Receiving an order by number")
    @Description("Проверка получения заказа по его номеру")
    public void testGetOrderByNumber() {
        ValidatableResponse getOrderResponse = client.getOrdersByCourier(String.valueOf(trackId));
        assertThat("Статус код неверный при получении заказа по его номеру",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Неверное сообщение при получении заказа по его номеру",
                getOrderResponse.extract().path("order"), notNullValue());
    }

    @Test
    @DisplayName("Receiving an order with an invalid number")
    @Description("Проверка получения заказа по недействительному номеру")
    public void testGetOrderByInvalidNumber() {
        int invalidTrackId = randomInt(500, 20000);
        ValidatableResponse getOrderResponse = client.getOrdersByCourier(String.valueOf(invalidTrackId));
        assertThat("Статус код неверный при запросе с недействительным номером заказа",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при запросе с недействительным номером заказа",
                getOrderResponse.extract().path("message"), equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Receiving an order without a number")
    @Description("Проверка получения заказа без номера")
    public void testGetOrderByEmptyNumber() {
        ValidatableResponse getOrderResponse = client.getOrdersByCourier("");
        assertThat("Статус код неверный при запросе заказа без номера",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Неверное сообщение при запросе заказа без номера",
                getOrderResponse.extract().path("message"), equalTo("Недостаточно данных для поиска"));
    }

    @After
    public void tearDown() {
        client.cancelOrder(trackId);
        courierClient.delete(courierId);
    }
}
