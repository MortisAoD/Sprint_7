import data.Orders;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Client;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;

import static order.Generator.randomOrder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class TestCreateOrder {

    private Client client;
    private Orders orders;
    private int trackId;
    private List<String> colour;

    @Before
    public void setUp() {
        client = new Client();
    }

    public TestCreateOrder(List<String> colour) {
        this.colour = colour;
    }

    @Parameters
    public static List<List<String>> getTestData() {
        return Arrays.asList(
                Arrays.asList("BLACK"),
                Arrays.asList("GREY"),
                Arrays.asList("GREY", "BLACK"),
                Arrays.asList()
        );
    }

    @Test
    @DisplayName("Creating an order with color selection")
    @Description("Проверка возможности создания заказа с выбором цвета")
    public void createOrderTest() {
        orders = randomOrder();
        orders.setColor(colour);
        ValidatableResponse response = client.create(orders);
        trackId = response.extract().path("track");
        assertThat("Неверный статус код при создании заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));
        assertThat("Неверный тип данных для идентификатора заказа",
                response.extract().path("track"), instanceOf(Integer.class));
    }

    @After
    public void tearDown() {
        client.cancelOrder(trackId);
    }
}
