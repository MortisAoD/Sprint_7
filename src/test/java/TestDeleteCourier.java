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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.Utils.randomInt;

public class TestDeleteCourier {

    private Client courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = randomCourier();
        courierClient = new Client();
        courierClient.create(courier);
        ValidatableResponse response = courierClient.login(certFrom(courier));
        courierId = response.extract().path("id");
    }

    @Test
    @DisplayName("Deleting a courier with an invalid ID")
    @Description("Проверка удаления курьера с несуществующим идентификатором")
    public void deleteCourierWithWrongIdTest() {
        ValidatableResponse response = courierClient.delete(randomInt(100000, 200000));
        assertThat("Неверный статус код при удалении курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при удалении курьера с несуществующим идентификатором",
                response.extract().path("message"), equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Courier Removal")
    @Description("Проверка удаления курьера")
    public void deleteCourierTest() {
        ValidatableResponse response = courierClient.delete(courierId);
        assertThat("Неверный статус код при удалении курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Неверное сообщение при успешном удалении курьера",
                response.extract().path("ok"), equalTo(true));
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }
}
