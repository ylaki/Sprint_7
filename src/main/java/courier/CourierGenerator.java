package courier;

import models.Courier;
import static utils.Utils.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin(randomString(10))
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }
    public static Courier nonRandomCourier() {
        return new Courier()
                .withLogin("DuplicatedTest111")
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }

    public static Courier noLoginCourier() {
        return new Courier()
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }

    public static Courier noPasswordCourier() {
        return new Courier()
                .withLogin(randomString(10))
                .withFirstName(randomString(20));
    }

}