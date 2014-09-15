package net.arwix.astronomy.events;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.coordinates.Location;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class RiseSetEventTest extends TestCase {

    private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");
    private Location location = new Location(Math.toRadians(30.3290233), Math.toRadians(59.909328));
    private Calendar date;

    public void setUp() throws Exception {
        super.setUp();
        date = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
        date.set(Calendar.YEAR, 2014);
        date.set(Calendar.MONTH, 8);
        date.set(Calendar.DAY_OF_MONTH, 14);
        date.set(Calendar.HOUR_OF_DAY, 20);
    }

    public void testGetRiseTime() throws Exception {
        RiseSetEvent riseSetEvent = new RiseSetEvent(ObjectType.Sun, VSOP87Objects.Sun);
        formater.setTimeZone(date.getTimeZone());
        riseSetEvent.setDate(date);
        riseSetEvent.setLocation(location);
        Calendar rise = riseSetEvent.getRiseTime();
        Assert.assertEquals("Sunrise test", formater.format(rise.getTime()), "2014-09-14 07:23:53 +0400");
    }

    public void testGetSetTime() throws Exception {
        RiseSetEvent riseSetEvent = new RiseSetEvent(ObjectType.Sun, VSOP87Objects.Sun);
        formater.setTimeZone(date.getTimeZone());
        riseSetEvent.setDate(date);
        riseSetEvent.setLocation(location);
        Calendar set = riseSetEvent.getSetTime();
        Assert.assertEquals("Sunset test", formater.format(set.getTime()), "2014-09-14 20:23:12 +0400");
    }

    public void testGetAbove() throws Exception {
        RiseSetEvent riseSetEvent = new RiseSetEvent(ObjectType.Sun, VSOP87Objects.Sun);
        formater.setTimeZone(date.getTimeZone());
        riseSetEvent.setDate(date);
        riseSetEvent.setLocation(location);
        boolean above = riseSetEvent.getAbove();
        Assert.assertEquals("SunRiseSet test", above, false);
    }
}

