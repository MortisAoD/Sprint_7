package data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourierCert {

    private final String login;
    private final String password;

    public static CourierCert certFrom(Courier courier) {
        return new CourierCert(courier.getLogin(), courier.getPassword());
    }
}