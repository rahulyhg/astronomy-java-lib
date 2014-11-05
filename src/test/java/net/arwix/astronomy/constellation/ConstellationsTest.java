package net.arwix.astronomy.constellation;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.arwix.astronomy.AstroMath;
import net.arwix.astronomy.Constant;
import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.coordinates.Matrix;
import net.arwix.astronomy.coordinates.SphericalVector;
import net.arwix.astronomy.coordinates.Vector;
import net.arwix.astronomy.coordinates.VectorType;

import java.util.Calendar;
import java.util.TimeZone;

public class ConstellationsTest extends TestCase {

    public void testCns_pick() throws Exception {

        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        date.set(Calendar.YEAR, 1850);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DAY_OF_MONTH, 1);
        CalendarMath.setHours(date, 12.0);
        final double T = CalendarMath.getJT(date);

        Vector vector = new SphericalVector(Math.toRadians(300), Math.toRadians(45));
        Matrix precession = AstroMath.getEquatorialPrecession(Constant.T_J2000, T);

        SphericalVector sphericalVector = (SphericalVector) precession.MultiplyMV(vector).getVectorInType(VectorType.SPHERICAL);
        String cons = Constellations.cns_pick(sphericalVector.phi, sphericalVector.theta);
        Assert.assertEquals("Identification of a Constellation From Position test ", "Cyg", cons);
    }
}