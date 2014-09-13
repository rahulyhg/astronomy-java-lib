package net.arwix.astronomy.calendar;

import net.arwix.astronomy.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static java.util.Calendar.*;

public abstract class CalendarMath {

    /**
     * Конвертация даты в строчку
     */
    public static class CalendarFormater {
        private static String sStringFormat = "yyyy-MM-dd HH:mm:ss ZZZZZ";

        private SimpleDateFormat mSimpleDateFormat;

        private CalendarFormater() {
            mSimpleDateFormat = new SimpleDateFormat(sStringFormat);
        }

        public String toString(Calendar calendar) {
            return mSimpleDateFormat.format(calendar.getTime());
        }

        public Calendar fromString(String string) {
            Calendar calendar = Calendar.getInstance();
            try {
                return fromString(string, calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Calendar fromString(String string, Calendar calendar) throws ParseException {
            calendar.setTime(mSimpleDateFormat.parse(string));
            return calendar;
        }
    }

    public static CalendarFormater getFormater() {
        return new CalendarFormater();
    }

    /**
     * Конвертация в UTC календарь
     *
     * @param localCalendar календарь в локальной временной зоне
     * @return календарь в UTC зоне
     */
    public static Calendar toUTC(Calendar localCalendar) {
        TimeZone tz = localCalendar.getTimeZone();
        //Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
        long msFromEpochGmt = localCalendar.getTime().getTime();
        //gives you the current offset in ms from GMT at the current date
        int offsetFromUTC = tz.getOffset(msFromEpochGmt);
        //create a new calendar in GMT timezone, set to this date and add the offset
        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utcCalendar.setTime(localCalendar.getTime());
        utcCalendar.add(Calendar.MILLISECOND, offsetFromUTC);
        return utcCalendar;
    }

    /**
     * Вычисление модифицированного юлианского дня с учетом временной зоны
     * <b> Важно: все числа даты и времени должны быть положительные </b>
     *
     * @param calendar календарь может быть в локальном времени
     * @return Модифицированная юлианская дата
     */
    public static double getMJD(Calendar calendar) {
        int b;

        int y = calendar.get(YEAR);
        int m = calendar.get(MONTH) + 1;

        if (m <= 2) {
            m += 12;
            --y;
        }

        if ((10000L * y + 100L * m + calendar.get(DATE)) <= 15821004L)
            b = -2 + ((y + 4716) / 4) - 1179;
        else
            b = (y / 400) - (y / 100) + (y / 4);

        long MJDN = 365 * y - 679004L + b + (int) (30.6001 * (m + 1)) + calendar.get(DATE);
        double MJDF = (Math.abs(calendar.get(HOUR_OF_DAY)) + Math.abs(calendar.get(MINUTE)) / 60.0 + Math
                .abs(calendar.get(SECOND) + calendar.get(MILLISECOND) / 1000.0) / 3600.0) / 24.0;

        return MJDN + MJDF - calendar.get(ZONE_OFFSET) / 24.0 / 60.0 / 60.0 / 1000.0;
    }

    /**
     * Вычисление юлианского столетия на эпоху J2000
     *
     * @param aMJD юлианская дата
     * @return J2000
     */
    public static double getJT(double aMJD) {
        return (aMJD - Constant.MJD_J2000) / 36525.0;
    }

    /**
     * Вычисление юлианского столетия на эпоху J2000
     *
     * @param calendar календарь может быть в локальном времени
     * @return J2000
     */
    public static double getJT(Calendar calendar) {
        return getJT(getMJD(calendar));
    }

    /**
     * <a href="http://eclipse.gsfc.nasa.gov/LEcat5/deltatpoly.html">POLYNOMIAL EXPRESSIONS FOR
     * DELTA T (ΔT)</a>
     *
     * @return возвращает разницу во времени между TT и UT в секундах
     */
    public static double getDeltaTofSecond(Calendar calendar) {
        final double y = calendar.get(YEAR) + (calendar.get(MONTH) + 1.0 - 0.5) / 12.0;
        double u, dt;
        if (y < 1986 || y > 2050) throw new IndexOutOfBoundsException();
        if (y < 2005) {
            u = y - 2000.0;
            dt = 63.86 + (0.3345 + (-0.060374 + (0.0017275 + (0.000651814
                    + 0.00002373599 * u) * u) * u) * u) * u;
        } else {
            u = y - 2000.0;
            dt = 62.92 + (0.32217 + 0.005589 * u) * u;
        }

        return dt;
    }

    /**
     * <a href="http://eclipse.gsfc.nasa.gov/LEcat5/deltatpoly.html">POLYNOMIAL EXPRESSIONS FOR
     * DELTA T (ΔT)</a>
     *
     * @return возвращает разницу во времени между ET и UT в долях суток
     */
    public static double getDeltaTofDay(Calendar calendar) {
        return getDeltaTofSecond(calendar) / 60.0 / 60.0 / 24.0;
    }

    /**
     * Среднее гринвичское звездное время
     * Greenwich Mean Sidereal Time
     *
     * @param aMJD Время в форме модифицированной юлианской даты
     * @return GMST в радианах
     */
    public static double getGMST(double aMJD) {
        final double SECS = 86400.0; // колличество секунд в сутках
        double MJD_0, UT, T_0, T, gmst;

        MJD_0 = Math.floor(aMJD);
        UT = SECS * (aMJD - MJD_0); // [сек]
        T_0 = (MJD_0 - 51544.5) / 36525.0;
        T = (aMJD - 51544.5) / 36525.0;
        gmst = 24110.54841 + 8640184.812866 * T_0 + 1.0027379093 * UT + (0.093104 - 0.0000062 * T)
                * T * T; // [сек]

        return (Constant.PI2 / SECS) * (gmst % SECS); // [рад]
    }

}