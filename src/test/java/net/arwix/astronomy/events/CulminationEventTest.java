package net.arwix.astronomy.events;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.coordinates.Location;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CulminationEventTest extends TestCase {

    private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");
    private Location location = new Location(Math.toRadians(30.3), Math.toRadians(60));
    private Calendar date;

    public void setUp() throws Exception {
        super.setUp();
        date = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
        date.set(Calendar.YEAR, 2014);
        date.set(Calendar.MONTH, 8);
        date.set(Calendar.DAY_OF_MONTH, 28);
        date.set(Calendar.HOUR_OF_DAY, 15);
    }

    public void testGetUpperTime() throws Exception {
        CulminationEvent culminationEvent = new CulminationEvent(ObjectType.Sun, VSOP87Objects.Sun);
        culminationEvent.setDate(date);
        culminationEvent.setLocation(location);
        Assert.assertEquals("Sun culmination upper test",
                formater.format(culminationEvent.getUpperTime().getTime()),
                "2014-09-28 13:49:04 +0400");
    }

    public void testIsUpperAbove() throws Exception {
        CulminationEvent culminationEvent = new CulminationEvent(ObjectType.Sun, VSOP87Objects.Sun);
        culminationEvent.setDate(date);
        culminationEvent.setLocation(location);
        Assert.assertEquals("Sun culmination upper test",
                culminationEvent.isUpperAbove(),
                true);
    }

    public void testGetLowerTime() throws Exception {
        CulminationEvent culminationEvent = new CulminationEvent(ObjectType.Sun, VSOP87Objects.Sun);
        culminationEvent.setDate(date);
        culminationEvent.setLocation(location);
        Assert.assertEquals("Sun culmination lower test",
                formater.format(culminationEvent.getLowerTime().getTime()),
                "2014-09-28 01:50:05 +0400");
    }

    public void testIsLowerAbove() throws Exception {
        CulminationEvent culminationEvent = new CulminationEvent(ObjectType.Sun, VSOP87Objects.Sun);
        culminationEvent.setDate(date);
        culminationEvent.setLocation(location);
        Assert.assertEquals("Sun culmination lower test",
                culminationEvent.isLowerAbove(),
                false);
    }
}