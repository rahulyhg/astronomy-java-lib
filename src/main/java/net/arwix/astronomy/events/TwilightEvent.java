package net.arwix.astronomy.events;


import net.arwix.astronomy.GeocentricEquatorialCoordinates;
import net.arwix.astronomy.VSOP87.VSOP87Objects;

import java.util.Calendar;

import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class TwilightEvent extends AbstractEvent {

    public static enum TwilightType {
        Civil(-6.0), Nautical(-12.0), Astronomical(-18.0);

        private double sinRefractionAngle;

        private TwilightType(double angle) {
            this.sinRefractionAngle = sin(toRadians(angle));
        }

        double getSinRefractionAngle() {
            return this.sinRefractionAngle;
        }

    }

    private TwilightType twilightType;

    public TwilightEvent() {
        super(VSOP87Objects.Sun);
    }

    public TwilightEvent(GeocentricEquatorialCoordinates SunCoordinates) {
        super(SunCoordinates);
    }

    public void setTwilightType(TwilightType type) {
        if (type != this.twilightType) {
            super.isValid = false;
            this.twilightType = type;
        }
    }

    public TwilightType getTwilightType() {
        return this.twilightType;
    }

    public Calendar getBeginEvent() {
        if (!super.isValid) {
            calc();
        }
        return super.riseDate;
    }

    public Calendar getEndEvent() {
        if (!super.isValid) {
            calc();
        }
        return super.downDate;
    }

    public boolean getAbove() {
        if (!super.isValid) {
            calc();
        }
        return super.above;
    }

    private void calc() {
        super.callCircleOfLatitudeRiseSet(twilightType.getSinRefractionAngle());
    }


}
