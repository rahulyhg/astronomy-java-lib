package net.arwix.astronomy.calendar;

import java.util.Calendar;

import static java.util.Calendar.*;

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

    public SimpleDate(SimpleDate date) {
        year = date.year;
        month = date.month;
        day = date.day;
        hour = date.hour;
        minute = date.minute;
        seconds = date.seconds;
        zoneOffset = date.zoneOffset;
    }

    /**
     * Устанавливает время в 00:00:00
     * не срасывает временую зону
     */
    public void resetTime() {
        hour = 0;
        minute = 0;
        seconds = 0.0;
    }


}