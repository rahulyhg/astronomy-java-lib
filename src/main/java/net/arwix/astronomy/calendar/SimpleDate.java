package net.arwix.astronomy.calendar;

import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.ZONE_OFFSET;

public class SimpleDate {

    public int year;
    // 1 - 12
    public int month;
    public int day;
    public int hour;
    public int minute;
    public double seconds;
    public int zoneOffset;

    public SimpleDate(Calendar calendar) {
        year = calendar.get(YEAR);
        month = calendar.get(MONTH) + 1;
        day = calendar.get(DATE);
        hour = calendar.get(HOUR_OF_DAY);
        minute = calendar.get(MINUTE);
        seconds = calendar.get(SECOND) + calendar.get(MILLISECOND) / 1000.0;
        zoneOffset = calendar.get(ZONE_OFFSET);
    }


}