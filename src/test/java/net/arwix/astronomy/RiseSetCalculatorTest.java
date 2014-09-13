package net.arwix.astronomy;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.calendar.CalendarMath;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class RiseSetCalculatorTest extends TestCase {

    public void testGetRiseSet() throws Exception {


        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

        RiseSetCalculator cals = new RiseSetCalculator(VSOP87Objects.Sun);
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
//        date.set(Calendar.ZONE_OFFSET, 60 * 60 * 1000);
        date.set(Calendar.YEAR, 2014);
        date.set(Calendar.MONTH, 8);
        date.set(Calendar.DAY_OF_MONTH, 14);
        date.set(Calendar.HOUR_OF_DAY, 20);
        formater.setTimeZone(date.getTimeZone());
//        RiseSetCalculator.Result result = cals.getRiseSet(RiseSetCalculator.Event.RiseSet, date, 30, 60);
        RiseSetCalculator.Result result = cals.getRiseSet(RiseSetCalculator.Event.RiseSet, date, 30.3290233, 59.909328);


        CalendarMath.CalendarFormater calendarFormater = CalendarMath.getFormater();


        Assert.fail(formater.format(result.rise.getTime()));
    }
}