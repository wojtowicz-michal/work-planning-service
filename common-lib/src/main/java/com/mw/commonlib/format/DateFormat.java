package com.mw.commonlib.format;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.mw.commonlib.util.Constants.DATE_TIME_PATTERN;
import static com.mw.commonlib.util.Constants.TIME_ZONE_HOURS;

public class DateFormat {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

    private DateFormat() {
    }

    public static DateTime getCurrentDateTime() {
        DateTime currentDateTime = new DateTime();
        return currentDateTime
                .plusHours(TIME_ZONE_HOURS)
                .minusSeconds(currentDateTime.getSecondOfMinute())
                .minusMillis(currentDateTime.getMillisOfSecond());
    }

    public static DateTime stringToDateTime(String date) {
        return DateTime.parse(date, FORMATTER);
    }

    public static String dateTimeToString(DateTime date) {
        return FORMATTER.print(date);
    }
}
