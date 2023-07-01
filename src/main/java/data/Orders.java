package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public Orders setColor(List<String> color) {
        this.color = color;
        return this;
    }

    public Orders setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Orders setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Orders setAddress(String address) {
        this.address = address;
        return this;
    }

    public Orders setMetroStation(String metroStation) {
        this.metroStation = metroStation;
        return this;
    }

    public Orders setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Orders setRentTime(int rentTime) {
        this.rentTime = rentTime;
        return this;
    }

    public Orders setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public Orders setComment(String comment) {
        this.comment = comment;
        return this;
    }
}