package vn.com.hugio.common.utils;

import java.time.*;

public class DateTimeUtil {

    public static long toUnix(LocalDateTime time, String... timeZone) {
        String zone = timeZone.length == 1 ? timeZone[0] : "Asia/Ho_Chi_Minh";
        return time.atZone(ZoneId.of(zone)).toEpochSecond();
    }

    public static long toUnixMil(LocalDateTime time, String... timeZone) {
        return toUnix(time, timeZone) * 1000;
    }

    public static LocalDateTime generateCurrentTimeDefault() {
        return ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
    }

    public static LocalTime generateCurrentLocalTimeDefault() {
        return ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalTime();
    }

    public static LocalDate generateCurrentLocalDateDefault() {
        return ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
    }
}
