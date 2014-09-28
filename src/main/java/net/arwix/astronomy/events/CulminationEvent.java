package net.arwix.astronomy.events;

import net.arwix.astronomy.GeocentricEquatorialCoordinates;
import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.math.SearchExtremumGoldenSection;

import java.util.Calendar;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class CulminationEvent extends AbstractEvent {

    private Calendar upperTime, lowerTime;
    private boolean upperAbove, lowerAbove;
    private double sinRefractionAngle;

    public CulminationEvent(ObjectType objectType, GeocentricEquatorialCoordinates coordinates) {
        super(coordinates);
        sinRefractionAngle = objectType.getSinRefractionAngle();
    }

    final protected void callCulmination(final double sinRefractionAngle) {
        isValid = false;
        resetTimeInnerDate();
        final double MJD0 = CalendarMath.getMJD(innerDate);
        final double cosLatitude = cos(location.latitude);
        final double sinLatitude = sin(location.latitude);

        SearchExtremumGoldenSection.Function function = new SearchExtremumGoldenSection.Function() {
            @Override
            public double calculation(double x) {
                return CulminationEvent.super.getSinAltitude(MJD0 + x / 24.0, location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle;
            }
        };

        SearchExtremumGoldenSection culmination = new SearchExtremumGoldenSection(function, 0, 24.0, Math.ulp(100), 50);

        upperTime = Calendar.getInstance(getDate().getTimeZone());
        upperTime.setTime(getDate().getTime());
        CalendarMath.setHours(upperTime, culmination.getMax());
        upperAbove = (getSinAltitude(CalendarMath.getMJD(upperTime), location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle) > 0.0;

        lowerTime = Calendar.getInstance(getDate().getTimeZone());
        lowerTime.setTime(getDate().getTime());
        CalendarMath.setHours(lowerTime, culmination.getMin());
        lowerAbove = (getSinAltitude(CalendarMath.getMJD(lowerTime), location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle) > 0.0;

        isValid = true;
    }

    public Calendar getUpperTime() {
        if (!isValid) {
            calc();
        }
        return upperTime;
    }

    public boolean isUpperAbove() {
        if (!isValid) {
            calc();
        }
        return upperAbove;
    }

    public Calendar getLowerTime() {
        if (!isValid) {
            calc();
        }
        return lowerTime;
    }

    public boolean isLowerAbove() {
        if (!isValid) {
            calc();
        }
        return lowerAbove;
    }

    private void calc() {
        callCulmination(sinRefractionAngle);
    }
}
