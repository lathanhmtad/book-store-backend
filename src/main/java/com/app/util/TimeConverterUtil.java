package com.app.util;

import java.util.concurrent.TimeUnit;

public class TimeConverterUtil {
    public static long getMilliseconds(String input) {
        int spaceIndex = input.indexOf(" ");
        String timeUnit = input.substring(spaceIndex + 1).toLowerCase();
        Long number = Long.valueOf(input.substring(0, spaceIndex));

        Long result = switch (timeUnit) {
            case "minute", "minutes" -> TimeUnit.MINUTES.toMillis(number);
            case "hour", "hours" -> TimeUnit.HOURS.toMillis(number);
            case "day", "days" -> TimeUnit.DAYS.toMillis(number);
            default -> throw new IllegalArgumentException(number + "is an unknown value!");
        };

        return result.longValue();
    }
}
