package me.hsgamer.gamesinthebox.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    private static final Map<Character, Long> TIME_UNIT_MAP = ImmutableMap.<Character, Long>builder()
            .put('s', TimeUnit.SECONDS.toMillis(1))
            .put('m', TimeUnit.MINUTES.toMillis(1))
            .put('h', TimeUnit.HOURS.toMillis(1))
            .put('d', TimeUnit.DAYS.toMillis(1))
            .put('w', TimeUnit.DAYS.toMillis(7))
            .put('M', TimeUnit.DAYS.toMillis(30))
            .put('y', TimeUnit.DAYS.toMillis(365))
            .build();

    private TimeUtil() {
        // EMPTY
    }

    public static long parseMillis(String time) {
        if (time == null || time.isEmpty()) {
            return 0;
        }
        long millis = 0;
        StringBuilder number = new StringBuilder();
        for (char c : time.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    millis += Long.parseLong(number.toString()) * TIME_UNIT_MAP.getOrDefault(c, 0L);
                    number = new StringBuilder();
                }
            }
        }
        if (number.length() > 0) {
            millis += Long.parseLong(number.toString());
        }
        return millis;
    }

    public static List<String> suggest(String time) {
        if (time == null || time.isEmpty() || Character.isAlphabetic(time.charAt(time.length() - 1))) {
            return ImmutableList.of("1s", "1m", "1h", "1d", "1w", "1M", "1y");
        }
        List<String> timeUnits = new ArrayList<>();
        TIME_UNIT_MAP.keySet().forEach(c -> timeUnits.add(time + c));
        return timeUnits;
    }

    public static String formatStandardTime(long millis) {
        return formatStandardTime(millis, TimeUnit.MILLISECONDS);
    }

    public static String formatStandardTime(long time, TimeUnit unit) {
        long millis = unit.toMillis(time);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
