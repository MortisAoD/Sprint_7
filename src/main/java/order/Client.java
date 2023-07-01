package order;

import data.OrderCert;
import data.Orders;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import utils.Specification;

import static io.restassured.RestAssured.given;

public class Client {
    private static final String PATH = "/api/v1/orders";
    private static final String TRACK_PATH = "/api/v1/orders/track";
    private static final String CANCEL_PATH = "/api/v1/orders/cancel";
    private static final String ACCEPT_PATH = "/api/v1/orders/accept/";

    @Step("Send post request to /api/v1/orders")
    public ValidatableResponse create(Orders order) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Send put request to /api/v1/orders/cancel")
    public ValidatableResponse cancelOrder(int trackId) {
        OrderCert orderCert = new OrderCert(trackId);
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(orderCert)
                .when()
                .put(CANCEL_PATH)
                .then();
    }

    @Step("Send get request to /api/v1/orders")
    public ValidatableResponse getOrders() {
        return given()
                .spec(Specification.requestSpecification())
                .get(PATH)
                .then();
    }

    @Step("Send get request to /api/v1/orders/track")
    public ValidatableResponse getOrdersByCourier(String courierId) {
        return given()
                .spec(Specification.requestSpecification())
                .queryParam("t", courierId)
                .when()
                .get(TRACK_PATH)
                .then();
    }

    @Step("Send put request to /api/v1/orders/accept")
    public ValidatableResponse acceptOrder(String orderID, String courierID) {
        return given()
                .spec(Specification.requestSpecification())
                .queryParam("courierId", courierID)
                .when()
                .put(ACCEPT_PATH + Integer.parseInt(orderID))
                .then();
    }
}