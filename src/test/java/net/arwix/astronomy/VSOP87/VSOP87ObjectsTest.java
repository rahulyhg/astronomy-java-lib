package net.arwix.astronomy.VSOP87;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.arwix.astronomy.Constant;
import net.arwix.astronomy.Epoch;
import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.coordinates.SphericalVector;
import net.arwix.astronomy.coordinates.Vector;
import net.arwix.astronomy.coordinates.VectorType;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class VSOP87ObjectsTest extends TestCase {

    private double t;

    public void setUp() throws Exception {
        super.setUp();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final double mjd = CalendarMath.getMJD(calendar);
        final double deltaT = CalendarMath.getDeltaTofDay(calendar);
        this.t = CalendarMath.getJT(mjd + deltaT);
    }

    public void testGetHeliocentricEclipticPosition() throws Exception {
        Vector vector;

        vector = VSOP87Objects.Mercury.getHeliocentricEclipticPosition(t, Epoch.J2000);
        Assert.assertEquals("Mercury Heliocentric Ecliptic Position Lon J2000", printLongH(vector), "271.37046");
        Assert.assertEquals("Mercury Equatorial Position Lat. J2000", printLatH(vector), "-4.79468");
        Assert.assertEquals("Mercury Equatorial Position r. J2000", getR(vector), 0.4630863050405151, Math.ulp(10.0));

        vector = VSOP87Objects.Saturn.getHeliocentricEclipticPosition(t, Epoch.J2000);
        Assert.assertEquals("Saturn Heliocentric Ecliptic Position Lon J2000", printLongH(vector), "233.80445");
        Assert.assertEquals("Saturn Equatorial Position Lat. J2000", printLatH(vector), "2.14987");
        Assert.assertEquals("Saturn Equatorial Position r. J2000", getR(vector), 9.934548790744495, Math.ulp(10.0));

    }

    public void testGetGeocentricEquatorialPosition() throws Exception {
        Vector vector;

        vector = VSOP87Objects.Sun.getGeocentricEquatorialPosition(t, Epoch.J2000);
        Assert.assertEquals("Sun Geo Equatorial Position R.A. J2000", printLong(vector), "11:37:21.573");
        Assert.assertEquals("Sun Geo Equatorial Position DEC. J2000", printLat(vector), "02 26 53.70");
        Assert.assertEquals("Sun Geo Equatorial Position r. J2000", getR(vector), 1.0052391393969098, Math.ulp(10.0));

        vector = VSOP87Objects.Sun.getGeocentricEquatorialPosition(t, Epoch.APPARENT);
        Assert.assertEquals("Sun Equatorial Position R.A. APPARENT", printLong(vector), "11:38:5.994");
        Assert.assertEquals("Sun Equatorial Position DEC. APPARENT", printLat(vector), "02 22 5.11");
        Assert.assertEquals("Sun Equatorial Position r. APPARENT", getR(vector), 1.00524067556578, Math.ulp(10.0));

        vector = VSOP87Objects.Mercury.getGeocentricEquatorialPosition(t, Epoch.J2000);
        Assert.assertEquals("Mercury Geo Equatorial Position R.A. J2000", printLong(vector), "13:09:24.257");
        Assert.assertEquals("Mercury Geo Equatorial Position DEC. J2000", printLat(vector), "-9 39 1.47");
        Assert.assertEquals("Mercury Geo Equatorial Position r. J2000", getR(vector), 1.0503725058158193, Math.ulp(10.0));

        vector = VSOP87Objects.Mercury.getGeocentricEquatorialPosition(t, Epoch.APPARENT);
        Assert.assertEquals("Mercury Equatorial Position R.A. APPARENT", printLong(vector), "13:10:9.809");
        Assert.assertEquals("Mercury Equatorial Position DEC. APPARENT", printLat(vector), "-9 43 35.87");
        Assert.assertEquals("Mercury Equatorial Position r. APPARENT", getR(vector), 1.0503287437866513, Math.ulp(10.0));

        vector = VSOP87Objects.Saturn.getGeocentricEquatorialPosition(t, Epoch.J2000);
        Assert.assertEquals("Saturn Geo Equatorial Position R.A. J2000", printLong(vector), "15:08:38.116");
        Assert.assertEquals("Saturn Geo Equatorial Position DEC. J2000", printLat(vector), "-15 31 2.11");
        Assert.assertEquals("Saturn Geo Equatorial Position r. J2000", getR(vector), 10.47351156783531, Math.ulp(10.0));

        vector = VSOP87Objects.Saturn.getGeocentricEquatorialPosition(t, Epoch.APPARENT);
        Assert.assertEquals("Saturn Equatorial Position R.A. APPARENT", printLong(vector), "15:09:27.099");
        Assert.assertEquals("Saturn Equatorial Position DEC. APPARENT", printLat(vector), "-15 34 14.04");
        Assert.assertEquals("Saturn Equatorial Position r. APPARENT", getR(vector), 10.472671071306953, Math.ulp(10.0));

    }

    public void testGetMercuryEquatorialPosition() throws Exception {
        Vector vector;
        vector = VSOP87Objects.Mercury.getGeocentricEquatorialPosition(t, Epoch.J2000);

        Assert.assertEquals("Mercury Equatorial Position R.A. J2000", printLong(vector), "13:09:24.257");
        Assert.assertEquals("Mercury Equatorial Position DEC. J2000", printLat(vector), "-9 39 1.47");
        Assert.assertEquals("Mercury Equatorial Position r. J2000", getR(vector), 1.0503725058158193, Math.ulp(10.0));


        vector = VSOP87Objects.Mercury.getGeocentricEquatorialPosition(t, Epoch.APPARENT);
        System.out.println(printLong(vector));
        System.out.println(printLat(vector));
    }

    private String printLongH(Vector p) {
        SphericalVector vector = (SphericalVector) p.getVectorInType(VectorType.SPHERICAL);
        return String.format(Locale.ENGLISH, "%1$02.5f", Math.toDegrees(vector.phi));
    }

    private String printLatH(Vector p) {
        SphericalVector vector = (SphericalVector) p.getVectorInType(VectorType.SPHERICAL);
        return String.format(Locale.ENGLISH, "%1$02.5f", Math.toDegrees(vector.theta));
    }

    private String printLong(Vector p) {
        SphericalVector vector = (SphericalVector) p.getVectorInType(VectorType.SPHERICAL);

        double hours = (Constant.DEG * vector.phi / 15.0);

        final int hour = (int) hours;
        final double minutes = (hours - hour) * 60.0;
        final int minute = (int) minutes;
        final double seconds = (minutes - minute) * 60.0;

        return String.format(Locale.ENGLISH, "%1$02d:%2$02d:%3$02.3f", hour, minute, seconds);
    }

    private String printLat(Vector p) {
        SphericalVector vector = (SphericalVector) p.getVectorInType(VectorType.SPHERICAL);

        int g = (int) Math.toDegrees(vector.theta);
        double mm = (Math.toDegrees(vector.theta) - g) * 60.0;
        int m = (int) mm;
        double s = (mm - m) * 60.0;

        return String.format(Locale.ENGLISH, "%1$02d %2$02d %3$02.2f", g, Math.abs(m), Math.abs(s));
    }

    private double getR(Vector p) {
        SphericalVector vector = (SphericalVector) p.getVectorInType(VectorType.SPHERICAL);
        return vector.r;
    }

}