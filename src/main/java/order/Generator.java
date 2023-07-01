package order;

import data.Orders;
import java.util.ArrayList;
import java.util.List;

import static utils.Utils.*;

public class Generator {

    public static Orders randomOrder() {
        return new Orders()
                .setFirstName(randomString(10))
                .setLastName(randomString(10))
                .setAddress(randomString(10))
                .setMetroStation(randomString(10))
                .setPhone(randomPhoneNumber())
                .setRentTime(randomInt(10, 100))
                .setDeliveryDate(randomDate(2022, 2023))
                .setComment(randomString(10))
                .setColor(initColorList());
    }

    private static List<String> initColorList() {
        List<String> colors = new ArrayList<>();
        colors.add(randomString(10));
        return colors;
    }
}