package courier;

import data.Courier;
import data.CourierCert;
import data.DeleteCourier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import utils.Specification;

import static io.restassured.RestAssured.given;

public class Client {

    private static final String API_BASE_PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "/login";
    private static final String DELETE_PATH = API_BASE_PATH + "/";

    @Step("Отправить POST-запрос на " + API_BASE_PATH)
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(Specification.requestSpecification())
                .body(courier)
                .when()
                .post(API_BASE_PATH)
                .then();
    }

    @Step("Отправить POST-запрос на " + API_BASE_PATH + LOGIN_PATH)
    public ValidatableResponse login(CourierCert cert) {
        return given()
                .spec(Specification.requestSpecification())
                .body(cert)
                .when()
                .post(API_BASE_PATH + LOGIN_PATH)
                .then();
    }

    @Step("Отправить DELETE-запрос на " + DELETE_PATH + ":id")
    public ValidatableResponse delete(int id) {
        DeleteCourier deleteCourier = new DeleteCourier(String.valueOf(id));
        return given()
                .spec(Specification.requestSpecification())
                .body(deleteCourier)
                .when()
                .delete(DELETE_PATH + id)
                .then();
    }
}