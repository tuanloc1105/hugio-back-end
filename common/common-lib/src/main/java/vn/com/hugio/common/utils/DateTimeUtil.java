package vn.com.hugio.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class DateTimeUtil {

    public static long toUnix(LocalDateTime time, String... timeZone) {
        String zone = timeZone.length == 1 ? timeZone[0] : "Asia/Ho_Chi_Minh";
        return time.atZone(ZoneId.of(zone)).toEpochSecond();
    }

    public static long toUnixMil(LocalDateTime time, String... timeZone) {
        return toUnix(time, timeZone) * 1000;
    }

    public static LocalDateTime unixToLocalDateTime(Long unix) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(unix),
                TimeZone.getDefault().toZoneId()
        );
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
