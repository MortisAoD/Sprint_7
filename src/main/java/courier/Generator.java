package courier;

import data.Courier;
import utils.Utils;

public class Generator {

    public static Courier randomCourier() {
        return new Courier()
                .setLogin(Utils.randomString(5))
                .setPassword(Utils.randomString(8))
                .setFirstName(Utils.randomString(10));
    }
}
