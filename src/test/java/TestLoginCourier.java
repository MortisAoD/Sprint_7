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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.Utils.randomString;

public class TestLoginCourier {

    private Client courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = randomCourier();
        courierClient = new Client();
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Authorization of the courier with the correct data")
    @Description("Проверка авторизации курьера с правильными данными")
    public void testLoginCourierWithCorrectCredentials() {
        ValidatableResponse response = courierClient.login(certFrom(courier));
        assertThat("Статус код неверный при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Неверный тип данных в поле 'id' при успешной авторизации курьера",
                response.extract().path("id"), instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Authorization of a courier with an incorrect username")
    @Description("Проверка авторизации курьера с неправильным логином")
    public void testLoginCourierWithWrongLogin() {
        Courier courierWithWrongLogin = new Courier(randomString(5), courier.getPassword(), courier.getFirstName());
        ValidatableResponse response = courierClient.login(certFrom(courierWithWrongLogin));
        assertThat("Статус код неверный при авторизации курьера с неправильным логином",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при авторизации курьера с неправильным логином",
                response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Authorization of a courier with an incorrect password")
    @Description("Проверка авторизации курьера с неправильным паролем")
    public void testLoginCourierWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier(courier.getLogin(), randomString(8), courier.getFirstName());
        ValidatableResponse response = courierClient.login(certFrom(courierWithWrongPassword));
        assertThat("Статус код неверный при авторизации курьера с неправильным паролем",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Неверное сообщение при авторизации курьера с неправильным паролем",
                response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier authorization without a password")
    @Description("Проверка авторизации курьера без пароля")
    public void testLoginCourierWithoutPassword() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());
        ValidatableResponse response = courierClient.login(certFrom(courierWithoutPassword));
        assertThat("Статус код неверный при авторизации курьера без пароля",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Неверное сообщение при авторизации курьера без пароля",
                response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier authorization without login")
    @Description("Проверка авторизации курьера без логина")
    public void testLoginCourierWithoutLogin() {
        Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
        ValidatableResponse response = courierClient.login(certFrom(courierWithoutLogin));
        assertThat("Статус код неверный при авторизации курьера без логина",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Неверное сообщение при авторизации курьера без логина",
                response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = courierClient.login(certFrom(courier));
        courierId = loginResponse.extract().path("id");
        courierClient.delete(courierId);
    }
}
