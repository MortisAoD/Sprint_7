import courier.Client;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static courier.Generator.randomCourier;
import static data.CourierCert.certFrom;
import static org.junit.Assert.assertEquals;

public class TestCreateCourier {

    private Client courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = randomCourier();
        courierClient = new Client();
    }

    @Test
    @DisplayName("Creating a courier")
    @Description("Проверка возможности создания курьера")
    public void createCourierTest() {
        ValidatableResponse response = courierClient.create(courier);
        assertEquals("Неверный статус код при создании курьера",
                HttpStatus.SC_CREATED, response.extract().statusCode());
        assertEquals("Неверное сообщение при успешном создании курьера",
                true, response.extract().path("ok"));
    }

    @Test
    @DisplayName("Creating two identical couriers")
    @Description("Проверка возможности создания двух одинаковых курьеров")
    public void createTwoIdenticalCouriersTest() {
        ValidatableResponse responseFirst = courierClient.create(courier);
        assertEquals("Неверный статус код при создании первого курьера",
                HttpStatus.SC_CREATED, responseFirst.extract().statusCode());
        ValidatableResponse responseSecond = courierClient.create(courier);
        assertEquals("Неверный статус код при создании курьера с уже существующим логином",
                HttpStatus.SC_CONFLICT, responseSecond.extract().statusCode());
        assertEquals("Некорректное сообщение об ошибке при создании курьера с уже используемым логином",
                "Этот логин уже используется. Попробуйте другой.", responseSecond.extract().path("message"));
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = courierClient.login(certFrom(courier));
        courierId = loginResponse.extract().path("id");
        courierClient.delete(courierId);
    }
}
