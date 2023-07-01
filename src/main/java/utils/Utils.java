package utils;

import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static String randomPhoneNumber() {
        int num1 = randomInt(1, 7);
        int num2 = randomInt(0, 8);
        int num3 = randomInt(0, 8);
        int set2 = randomInt(100, 742);
        int set3 = randomInt(1000, 9999);

        return "+7 (" + num1 + num2 + num3 + ")-" + set2 + "-" + set3;
    }

    public static String randomDate(int startYear, int endYear) {
        int day = randomInt(1, 28);
        int month = randomInt(1, 12);
        int year = randomInt(startYear, endYear);

        return LocalDate.of(year, month, day).toString();
    }
}