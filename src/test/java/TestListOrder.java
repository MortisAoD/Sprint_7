import data.Orders;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Client;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static order.Generator.randomOrder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestListOrder {

    private Client client;
    private Orders orders;
    private int trackId;

    @Before
    public void setUp() {
        client = new Client();
        orders = randomOrder();
        ValidatableResponse createOrderResponse = client.create(orders);
        trackId = createOrderResponse.extract().path("track");
    }

    @Test
    @DisplayName("Getting a list of orders")
    @Description("Проверка получения списка заказов")
    public void testGetListOrders() {
        ValidatableResponse response = client.getOrders();
        assertThat("Статус код неверный при получении списка заказов",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Список заказов пустой",
                response.extract().path("orders"), notNullValue());
    }

    @After
    public void tearDown() {
        client.cancelOrder(trackId);
    }
}
